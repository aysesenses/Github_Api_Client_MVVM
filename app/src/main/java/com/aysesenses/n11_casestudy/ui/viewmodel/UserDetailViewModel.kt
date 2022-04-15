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
class UserDetailViewModel @Inject constructor(private  val githubApiService: GithubApiService, private val repository: RoomRepository,) : ViewModel() {
    private val _userDetail = MutableLiveData<UserDetail>()

    val userDetail: LiveData<UserDetail>
        get() = _userDetail

    //
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

    fun favorite(login: String, imageView: ImageView) : Boolean{
        Log.e("f",login)
        var status = false
        viewModelScope.launch {
            val list = repository.getUserFavoriteStatus(login)
            Log.e("before",list[0].favorite.toString())
            if (list[0].favorite == "no"){
                repository.addFavorite(login)
                Log.e("add","add favorite")
                status = true
            }else{
                repository.removeFavorite(login)
                Log.e("remove","remove favorite")
            }
            val list2 = repository.getUserFavoriteStatus(login)
            Log.e("after",list2[0].favorite.toString())
            //!!!
        // loadFromCache(list[0].term.toString())
            updateImage(imageView,status)

        }

        return status
    }
}