package com.aysesenses.data.local.repository

import com.aysesenses.data.local.dao.AppDao
import com.aysesenses.data.local.entitiy.UserEntity
import com.aysesenses.domain.model.User
import javax.inject.Inject

class RoomRepository @Inject constructor(private val appDao: AppDao) {

    suspend fun getSearchResults(term: String): List<UserEntity> {
        return appDao.getSearchResults(term)
    }

    suspend fun getFavorites(term: String): List<UserEntity> {
        return appDao.getFavorites(term)
    }

    suspend fun insertSearchResults(userEntities: List<UserEntity>) {
        appDao.insertSearchResults(userEntities)
    }

    suspend fun deleteSearchResults(term: String) {
        appDao.deleteSearchResults(term)
    }

    suspend fun getUserFavoriteStatus(term: String): List<UserEntity> {
        return appDao.getUserFavoriteStatus(term)
    }

    suspend fun addFavorite(term: String) {
        appDao.addFavorite(term)
    }

    suspend fun removeFavorite(term: String) {
        appDao.removeFavorite(term)
    }
}