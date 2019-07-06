package com.feelsokman.net.net

import com.feelsokman.net.net.model.NetResponseEmote
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET

class EmoteApi(retrofit: Retrofit) {

    private val api: Api = retrofit.create(Api::class.java)

    fun getEmotes() = api.getEmotes()

    interface Api {

        @GET("2/emotes")
        fun getEmotes(): Single<NetResponseEmote>
    }
}