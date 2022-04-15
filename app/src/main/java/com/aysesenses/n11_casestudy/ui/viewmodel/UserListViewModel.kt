package com.aysesenses.n11_casestudy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.aysesenses.data.firebase.FirestoreRepository
import com.aysesenses.data.local.entitiy.UserEntity
import com.aysesenses.data.local.repository.RoomRepository
import com.aysesenses.data.network.api.GithubApiService
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: RoomRepository,
    private val firestoreRepository: FirestoreRepository,
    private  val githubApiService: GithubApiService,
) :
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
        loadFromCache(term)
        viewModelScope.launch {
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
                firestoreRepository.saveSearchResults(mutableMap,term)
            } catch (e: Exception) {
                Log.e("userListErr", e.message.toString())
            }
        }
    }

    //This method to favorite users
    fun favorite(login: String){
        viewModelScope.launch {
            val list = repository.getUserFavoriteStatus(login)
            if (list.isNotEmpty()){
                if (list[0].favorite == "no"){
                    repository.addFavorite(login)
                }else{
                    repository.removeFavorite(login)
                }
                loadFromCache(list[0].term.toString())
            }
        }

    }

    private fun updateSearchResults(userEntities: List<UserEntity>, term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favs = repository.getFavorites(term)
            repository.insertSearchResults(userEntities)
            if (favs.isNotEmpty()){
                favs.forEach {
                    favorite(it.login.toString())
                }
            }
            loadFromCache(term)
        }
    }

    //Re-returns the results from the database
    private fun loadFromCache(term: String) {
        viewModelScope.launch() {
            val list = repository.getSearchResults(term)
            if (list.isNotEmpty()){
                _users.value = list
            }else{
                Log.e("boş","boş dürüm")
            }
        }
    }
}