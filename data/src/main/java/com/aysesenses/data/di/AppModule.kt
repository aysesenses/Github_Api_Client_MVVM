package com.aysesenses.data.di

import android.app.Application
import com.aysesenses.data.local.AppDatabase
import com.aysesenses.data.local.dao.AppDao
import com.aysesenses.data.network.api.GithubApiService
import com.aysesenses.domain.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APPModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideUsersRef(db: FirebaseFirestore) = db.collection("users")

    @Singleton
    @Provides
    fun getRetrofitInstance() : Retrofit{
        return  Retrofit.Builder()
            .baseUrl(Constants.GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getRetrofitServiceInterface (retrofit: Retrofit) : GithubApiService{
        return  retrofit.create(GithubApiService::class.java)
    }

    @Singleton
    @Provides
    fun getAppDB(context: Application): AppDatabase {
        return AppDatabase.getAppDB(context)
    }

    @Singleton
    @Provides
    fun getDao(appDB: AppDatabase): AppDao {
        return appDB.getDAO()
    }
}