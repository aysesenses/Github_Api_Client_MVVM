package com.aysesenses.n11_casestudy.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserAvatarViewModel : ViewModel() {
    val userAvatarUrl = MutableLiveData<String>()
}