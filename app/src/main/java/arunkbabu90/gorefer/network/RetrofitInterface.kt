package arunkbabu90.gorefer.network

import arunkbabu90.gorefer.model.Post
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitInterface {
    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        val instance: RetrofitInterface by lazy {
            return@lazy Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitInterface::class.java)
        }
    }

    @GET("posts")
    fun getPosts(): Call<List<Post>>
}