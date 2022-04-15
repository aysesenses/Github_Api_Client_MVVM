package com.aysesenses.n11_casestudy.ui.viewmodel

import com.aysesenses.domain.model.UserDetail
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aysesenses.data.local.repository.RoomRepository
import com.aysesenses.data.network.api.GithubApiService
import com.aysesenses.n11_casestudy.ui.adapter.updateImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val githubApiService: GithubApiService,
    private val repository: RoomRepository,
) : ViewModel() {

    // Internally, we use a MutableLiveData, because we will be updating the  UserDetail
    // with new values
    private val _userDetail = MutableLiveData<UserDetail>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val userDetail: LiveData<UserDetail>
        get() = _userDetail

    fun getUserDetails(userLogin: String) {
        viewModelScope.launch {
            val getPropertiesDeferred = githubApiService.getUserDetails(userLogin)
            try {
                _userDetail.value = getPropertiesDeferred.body()
            } catch (e: Exception) {
                Log.e("userDetailErr", e.message.toString())
            }
        }
    }

    fun favorite(login: String, imageView: ImageView): Boolean {
        var status = false
        viewModelScope.launch {
            val list = repository.getUserFavoriteStatus(login)
            if (list[0].favorite == "no") {
                repository.addFavorite(login)
                status = true
            } else {
                repository.removeFavorite(login)
            }
            updateImage(imageView, status)
        }
        return status
    }

    fun checkFavImage(login: String?, imageView: ImageView) {
        viewModelScope.launch {
            if (login?.let { repository.getUserFavoriteStatus(it)[0].favorite } == "yes") {
                updateImage(imageView, true)
            }
        }

    }
}