package com.aysesenses.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aysesenses.data.local.entitiy.UserEntity

@Dao
interface AppDao {

    @Query("SELECT * FROM user WHERE term = :term ORDER BY id DESC")
    suspend fun getSearchResults(term: String): List<UserEntity>

    @Query("SELECT * FROM user WHERE term = :term AND favorite ='yes' ORDER BY id DESC")
    suspend fun getFavorites(term: String): List<UserEntity>

    @Query("SELECT id, term, login, avatar_url, favorite FROM user WHERE login = :login ORDER BY id DESC")
    suspend fun getUserFavoriteStatus(login: String): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(userEntities: List<UserEntity>)

    @Query("DELETE FROM user WHERE term = :term")
    suspend fun deleteSearchResults(term: String)

    @Query("UPDATE user SET favorite = 'yes' WHERE login = :term")
    suspend fun addFavorite(term: String)

    @Query("UPDATE user SET favorite = 'no' WHERE login = :term")
    suspend fun removeFavorite(term: String)
}