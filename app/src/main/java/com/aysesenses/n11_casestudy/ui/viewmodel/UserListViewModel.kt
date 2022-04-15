package com.aysesenses.n11_casestudy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aysesenses.data.firebase.FirestoreRepository
import com.aysesenses.data.local.entitiy.UserEntity
import com.aysesenses.data.local.repository.RoomRepository
import com.aysesenses.data.network.api.GithubApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val repository: RoomRepository, private val firestoreRepository: FirestoreRepository, private  val githubApiService: GithubApiService) :
    ViewModel() {

    // Internally, we use a MutableLiveData, because we will be updating the List of UserEntitiy
    // with new values
    private val _users = MutableLiveData<List<UserEntity>?>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val users: LiveData<List<UserEntity>?>
        get() = _users

    private val userEntities: MutableList<UserEntity> = mutableListOf()
    private val mutableMap: MutableMap<String?, Any?> = mutableMapOf()

    fun userSearch(term: String) {
        viewModelScope.launch {
            loadFromCache(term)
            val getPropertiesDeferred = githubApiService.searchUser(term)
            try {

                //A successful response should include  returned all users with response.body()
                val result = getPropertiesDeferred.body()

                result?.users?.forEach {
                    userEntities.add(
                        UserEntity(
                            term = term,
                            login = it.login,
                            avatar_url = it.avatar_url,
                            favorite = "no"
                        )
                    )
                    //This map will save all incoming users from firebase
                    mutableMap.put(term,userEntities)
                }

                updateSearchResults(userEntities, term)

                //_users.value = repository.getSearchResults(term)

                //Save all users in Firebase
                firestoreRepository.saveSearchResults(mutableMap,term)
            } catch (e: Exception) {
                Log.e("userListErr", e.message.toString())
            }
        }
    }

    //This methot to favorite users
    fun favorite(login: String){
        viewModelScope.launch {
            val list = repository.getUserFavoriteStatus(login)
            Log.e("before",list[0].favorite.toString())

            if (list[0].favorite == "no"){
                repository.addFavorite(login)
                Log.e("add","add favorite")
            }else{
                repository.removeFavorite(login)
                Log.e("remove","remove favorite")
            }
            val list2 = repository.getUserFavoriteStatus(login)
            Log.e("after",list2[0].favorite.toString())

            //!!!
            loadFromCache(list[0].term.toString())
        }

    }


    private fun updateSearchResults(userEntities: List<UserEntity>, term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favs = repository.getUserFavoriteStatus(term)
            clearSearchResults(term)
            repository.insertSearchResults(userEntities)
            favs.forEach {
                favorite(it.login.toString())
            }
            loadFromCache(term)
        }
    }

    //Re-returns the results from the database
    private fun loadFromCache(term: String) {
        viewModelScope.launch() {
            val list = repository.getSearchResults(term)
            _users.value = list
        }
    }

    //Deletes all results in the database
    private fun clearSearchResults(term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSearchResults(term)
        }
    }

    private fun getFavorites(term: String): List<UserEntity>? {
        var favs: List<UserEntity>? = null
        viewModelScope.launch {
            favs = repository.getFavorites(term)
        }
        return favs
    }
}