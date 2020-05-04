package com.raywenderlich.githubrepolist.ui.activities
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.githubrepolist.R
import com.raywenderlich.githubrepolist.api.RepositoryRetriever
import com.raywenderlich.githubrepolist.data.RepoResult
import com.raywenderlich.githubrepolist.ui.adapters.RepoListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MainActivity : Activity() {
  private val repoRetriever = RepositoryRetriever()
  private val callback = object : Callback<RepoResult> {
    override fun onFailure(call: Call<RepoResult>?, t:Throwable?) {
      Log.e("MainActivity", "Problem calling Github API {${t?.message}}")
    }

    override fun onResponse(call: Call<RepoResult>?, response: Response<RepoResult>?) {
      response?.isSuccessful.let {
        val resultList = RepoResult(response?.body()?.items ?: emptyList())
        repoList.adapter = RepoListAdapter(resultList)
      }
    }
  }
  @RequiresApi(Build.VERSION_CODES.M)
  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    refreshButton.setOnClickListener {
      retrieveRepositories()
    }
    repoList.layoutManager = LinearLayoutManager(this)
    val url = "https://api.github.com/search/repositories?q=mario+language:kotlin&sort=stars&order=desc"
    if (isNetworkConnected()) {
      retrieveRepositories()
    }
    else {
      AlertDialog.Builder(this).setTitle("No Internet Connection")
              .setMessage("Please check your internet connection and try again")
              .setPositiveButton(android.R.string.ok) { _, _ -> }
              .setIcon(android.R.drawable.ic_dialog_alert).show()
    }
    }
  private fun retrieveRepositories() {
    //1 Create a Coroutine scope using a job to be able to cancel when needed
    val mainActivityJob = Job()

    //2 Handle exceptions if any
    val errorHandler = CoroutineExceptionHandler { _, exception ->
      AlertDialog.Builder(this).setTitle("Error")
              .setMessage(exception.message)
              .setPositiveButton(android.R.string.ok) { _, _ -> }
              .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    //3 the Coroutine runs using the Main (UI) dispatcher
    val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
    coroutineScope.launch(errorHandler) {
      //4
      val resultList = RepositoryRetriever().getRepositories()
      repoList.adapter = RepoListAdapter(resultList)
    }
  }
  @RequiresApi(Build.VERSION_CODES.M)
  private fun isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    return networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
  }
  }


