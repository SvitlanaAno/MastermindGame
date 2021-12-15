package com.perlovka.mastermindgame

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//The root web address of the Random Number server endpoint
private const val BASE_URL = "https://www.random.org/"

//Create the Retrofit object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getNumber] method
 */
interface SecretNumberApiService {
    @GET("integers")
    suspend fun getNumber(@Query("num") num: Int,
                          @Query("min") min: Int, @Query("max") max: Int,
                          @Query("col") col: Int, @Query("base") base: Int,
                          @Query("format")  format: String, @Query("rnd") rnd: String): String
}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object SecretNumberApi {
    val retrofitService : SecretNumberApiService by lazy {
        retrofit.create(SecretNumberApiService::class.java)
    }
}
