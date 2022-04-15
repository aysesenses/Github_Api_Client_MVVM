package com.aysesenses.data.network.response

import com.aysesenses.domain.model.User
import com.google.gson.annotations.SerializedName

class UserListResponse (
    @SerializedName("items")
    val users: List<User>
)