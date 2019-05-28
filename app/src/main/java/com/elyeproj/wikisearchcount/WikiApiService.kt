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
        val okHttpClient by lazy {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(okReplayInterceptor)
            builder.build()
        }

        fun create(): WikiApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://en.wikipedia.org/w/")
                    .client(okHttpClient)
                    .build()

            return retrofit.create(WikiApiService::class.java)
        }
    }

}