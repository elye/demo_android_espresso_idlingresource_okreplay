package com.elyeproj.wikisearchcount

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okreplay.OkReplayInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApiService {
    @GET("api.php")
    fun hitCountCheck(@Query("action") action: String,
                       @Query("format") format: String,
                       @Query("list") list: String,
                       @Query("srsearch") srsearch: String): Observable<Model.Result>

    companion object {
        val okReplayInterceptor = OkReplayInterceptor()

        fun create(): WikiApiService {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(okReplayInterceptor)

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://en.wikipedia.org/w/")
                    .client(builder.build())
                    .build()

            return retrofit.create(WikiApiService::class.java)
        }
    }

}