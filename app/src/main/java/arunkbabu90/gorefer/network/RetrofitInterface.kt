package arunkbabu90.gorefer.network

import arunkbabu90.gorefer.model.Post
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        val instance: RetrofitInterface by lazy {
            return@lazy Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitInterface::class.java)
        }
    }

    @GET("posts")
    fun getAllPosts(): Call<List<Post>>

    @GET("posts")
    fun getPosts(@Query("userId") pageNo: Int): Call<List<Post>>

}