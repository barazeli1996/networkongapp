package com.raywenderlich.githubrepolist.api
import com.raywenderlich.githubrepolist.data.RepoResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RepositoryRetriever {
    private val service: GithubService
    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(GithubService::class.java)
    }
    suspend fun getRepositories(): RepoResult  {
        return service.searchRepositories()
    }
}
