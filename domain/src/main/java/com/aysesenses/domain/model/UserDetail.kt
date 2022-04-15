package com.aysesenses.domain.model
import com.google.gson.annotations.SerializedName

 data class UserDetail (
    @SerializedName("login") val login : String?,
    @SerializedName("avatar_url") val avatar_url : String?,
    @SerializedName("name") val name : String?,
    @SerializedName("public_repos") val repos : Int?,
    @SerializedName("public_gists") val gists : Int?,
    @SerializedName("followers") val followers : Int?,
    @SerializedName("following") val following : Int?
)