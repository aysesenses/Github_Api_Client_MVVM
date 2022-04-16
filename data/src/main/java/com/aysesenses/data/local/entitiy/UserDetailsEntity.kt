package com.aysesenses.data.local.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_details", indices = [Index(value = ["login"], unique = true)])
class UserDetailsEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "avatar_url") val avatar_url: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "public_repos") val repos: Int?,
    @ColumnInfo(name = "public_gists") val gists: Int?,
    @ColumnInfo(name = "followers") val followers: Int?,
    @ColumnInfo(name = "following") val following: Int?
)