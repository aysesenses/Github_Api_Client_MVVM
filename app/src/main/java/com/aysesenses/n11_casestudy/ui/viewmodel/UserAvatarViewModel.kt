package com.aysesenses.n11_casestudy.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserAvatarViewModel : ViewModel() {

    // Internally, we use a MutableLiveData, because we will be updating the List of UserEntitiy
    // with new values
    val userAvatarUrl = MutableLiveData<String>()
}