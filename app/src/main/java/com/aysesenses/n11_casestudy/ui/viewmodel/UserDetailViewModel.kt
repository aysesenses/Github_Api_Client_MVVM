package com.aysesenses.n11_casestudy.ui.viewmodel

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aysesenses.data.local.entitiy.UserDetailsEntity
import com.aysesenses.data.local.repository.RoomRepository
import com.aysesenses.data.network.api.GithubApiService
import com.aysesenses.n11_casestudy.ui.adapter.updateImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val githubApiService: GithubApiService,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    // Internally, we use a MutableLiveData, because we will be updating the  UserDetail
    // with new values
    private val _userDetail = MutableLiveData<UserDetailsEntity>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val userDetail: LiveData<UserDetailsEntity>
        get() = _userDetail

    //With keyword the results are retrieved first
    //from the local database and then from the api query
    fun getUserDetails(userLogin: String) {
        loadFromCache(userLogin)
        viewModelScope.launch {
            val getPropertiesDeferred = githubApiService.getUserDetails(userLogin)
            try {
                val result = getPropertiesDeferred.body()
                val userDetailsEntity = UserDetailsEntity(
                    followers = result?.followers,
                    following = result?.following,
                    avatar_url = result?.avatar_url,
                    login = result?.login,
                    gists = result?.gists,
                    name = result?.name,
                    repos = result?.repos
                )
                updateUserDetail(userDetailsEntity, userLogin)

            } catch (e: Exception) {
                Log.e("userDetailError", e.message.toString())
            }
        }
    }

    private fun updateUserDetail(userDetailsEntity: UserDetailsEntity?, login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.insertUserDetail(userDetailsEntity)
            loadFromCache(login)
        }
    }

    fun favorite(login: String, imageView: ImageView) : Boolean{
        var status = false
        viewModelScope.launch {
            val list = roomRepository.getUserFavoriteStatus(login)
            if (list[0].favorite == "no"){
                roomRepository.addFavorite(login)
                status = true
            }else{
                roomRepository.removeFavorite(login)
            }
            updateImage(imageView,status)
        }
        return status
    }

    fun checkFavImage(login: String?, imageView: ImageView) {
        viewModelScope.launch {
            if (login?.let { roomRepository.getUserFavoriteStatus(it)[0].favorite } == "yes") {
                updateImage(imageView, true)
            }
        }
    }

    //The results retrieving from the database
    private fun loadFromCache(login: String) {
        viewModelScope.launch() {
            val userDetail = roomRepository.getUserDetail(login)
                _userDetail.value = userDetail
        }
    }
}