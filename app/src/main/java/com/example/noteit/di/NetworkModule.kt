package com.example.noteit.di

import com.example.noteit.api.AuthInterceptor
import com.example.noteit.api.NoteApi
import com.example.noteit.api.UserApi
import com.example.noteit.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit.Builder):UserApi{
        return retrofit.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor):OkHttpClient{
     return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()

    }


    @Singleton
    @Provides
    fun provideNoteApi(retrofit: Retrofit.Builder,okHttpClient: OkHttpClient):NoteApi{
        return retrofit.
        client(okHttpClient).
        build().create(NoteApi::class.java)
    }



}