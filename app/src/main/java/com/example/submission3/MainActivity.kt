package com.example.submission3

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.databinding.ActivityMainBinding
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.example.submission3.favourite.FavoriteActivity
import com.example.submission3.helper.MappingHelper
import com.example.submission3.menu.NotificationActivity
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Error

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var listUser = ArrayList<User>()
    private lateinit var uriWithId: Uri
    private lateinit var adapterClass : ListViewAdapter

    companion object{
        private const val STATE_RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listUser = ArrayList()

        if (savedInstanceState != null) {
            val result = savedInstanceState.getParcelableArrayList<User>(STATE_RESULT) as ArrayList<User>
            listUser = result
        }
        showRecycleView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.textView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                listUser.clear()
                if(query?.isNotEmpty()!!){
                    val client = AsyncHttpClient()
                    client.addHeader("User-Agent", "request")
                    //variabel GITHUB_TOKEN di gradle tidak terdeteksi disini
                    client.addHeader("Authorization", "token 24372cf649be896b10eca81777ed029e6df3c2e8")
                    val url = "https://api.github.com/search/users?q=$query"
                    client.get(url, object : AsyncHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                            binding.progressBar.visibility = View.GONE
                            val result = responseBody?.let { String(it) }
                            try {
                                val arrJson = JSONObject(result)
                                val item = arrJson.getJSONArray("items")
                                val totalSize = arrJson.getInt("total_count")

                                if(item.length() == 0){
                                    Snackbar.make(binding.textView, "Username tidak ditemukan!", Snackbar.LENGTH_LONG).show()
                                    binding.textView.visibility = View.VISIBLE
                                    listUser.clear()
                                    showRecycleView()
                                }
                                else{
                                    for (i in 0 until totalSize) {
                                        val objJson = item.getJSONObject(i)
                                        val username: String = objJson.getString("login")
                                        getDetail(username)
                                    }
                                }

                            } catch (e: Exception) {
                                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(statusCode: Int,headers: Array<Header>,responseBody: ByteArray,error: Throwable) {
                            binding.progressBar.visibility = View.GONE
                            val errorMessage = error.message
                            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.menu_favorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
            R.id.menu_notification -> {
                val mIntent = Intent(this, NotificationActivity::class.java)
                startActivity(mIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getDetail(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 24372cf649be896b10eca81777ed029e6df3c2e8")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int,headers: Array<Header>,responseBody: ByteArray) {
                binding.progressBar.visibility = View.GONE
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val namaUser: String = jsonObject.getString("login").toString()
                    val name: String = jsonObject.getString("name").toString()
                    val avatar: String = jsonObject.getString("avatar_url").toString()
                    val company: String = jsonObject.getString("company").toString()
                    val location: String = jsonObject.getString("location").toString()
                    val repository: String = jsonObject.getString("public_repos").toString()
                    val followers: String = jsonObject.getString("followers").toString()
                    val following: String = jsonObject.getString("following").toString()

                    val userData = User(name,namaUser,avatar,followers, following,repository,company,location)
                    listUser.add(userData)
                    showRecycleView()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int,headers: Array<Header>,responseBody: ByteArray,error: Throwable) {
                binding.progressBar.visibility = View.GONE
                val errorMessage = error.message
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun showRecycleView(){
        if(listUser.isEmpty()) binding.textView.visibility = View.VISIBLE
        else binding.textView.visibility = View.GONE

        adapterClass = ListViewAdapter(listUser)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapterClass

        adapterClass.init_callableItemRV(object : ListViewAdapter.InterfaceRV{
            override fun callableFunctionRV(item: User) {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                try {
                    uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + item.username)
                    val cursor = contentResolver.query(uriWithId, null, null, null, null)

                    if (cursor != null && cursor.moveToFirst()) {
                        item.isFavourite = MappingHelper().mapCursorToObject(cursor).isFavourite.toString()
                        cursor.close()
                    }
                    else{
                        item.isFavourite = "0"
                    }
                }catch (e : Error){

                }

                intent.putExtra(ProfileActivity.get_data, item)
                startActivity(intent)
            }
        })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_RESULT, listUser)
    }
}