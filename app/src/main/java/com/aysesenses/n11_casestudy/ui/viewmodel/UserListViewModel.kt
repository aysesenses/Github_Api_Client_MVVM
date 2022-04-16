package com.aysesenses.n11_casestudy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.aysesenses.data.firebase.FirestoreRepository
import com.aysesenses.data.local.entitiy.UserEntity
import com.aysesenses.data.local.repository.RoomRepository
import com.aysesenses.data.network.api.GithubApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val firestoreRepository: FirestoreRepository,
    private val githubApiService: GithubApiService,
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

    //With keyword "term" the results are retrieved first
    //from the local database and then from the api query
    fun userSearch(term: String?) {
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
                    mutableMap.put(term, userEntities)
                }
                updateSearchResults(userEntities, term)
                firestoreRepository.saveSearchResults(mutableMap, term)
            } catch (e: Exception) {
                Log.e("UserListViewModel", e.message.toString())
            }
        }
    }

    //This method to favorite users
    fun favorite(login: String?) {
        viewModelScope.launch {
            try {
                val list = roomRepository.getUserFavoriteStatus(login)
                if (list.isNotEmpty()) {
                    if (list[0].favorite == "no") {
                        roomRepository.addFavorite(login)
                    } else {
                        roomRepository.removeFavorite(login)
                    }
                    loadFromCache(list[0].term.toString())
                }
            } catch (e: Exception) {
                Log.e("UserListViewModel", e.message.toString())
            }
        }
    }

    //The current favorite information is retrieved from the local database and
    //The results from the api are added to the local database
    private fun updateSearchResults(userEntities: List<UserEntity>, term: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favorites = roomRepository.getFavorites(term)
                roomRepository.insertSearchResults(userEntities)
                if (!favorites.isNullOrEmpty()) {
                    favorites.forEach {
                        favorite(it.login.toString())
                    }
                }
                loadFromCache(term)
            } catch (e: Exception) {
                Log.e("UserListViewModel", e.message.toString())
            }
        }
    }

    //The results retrieving from the database
    private fun loadFromCache(term: String?) {
        viewModelScope.launch() {
            try {
                val list = roomRepository.getSearchResults(term)
                _users.value = list
            } catch (e: Exception) {
                Log.e("UserListViewModel", e.message.toString())
            }
        }
    }
}