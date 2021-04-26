package com.example.consumerapp.favourite

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.User
import com.example.consumerapp.databinding.ActivityFavoriteBinding
import com.example.consumerapp.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    lateinit var binding : ActivityFavoriteBinding
    private var listUser = ArrayList<User>()
    lateinit var  adapterClass : FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Favorite ConsumerApp"
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        adapterClass = FavoriteAdapter()
        binding.rvFavorite.adapter = adapterClass


        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadDataAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun loadDataAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)

                MappingHelper().mapCursorToArrayList(cursor)
            }
            listUser = deferredNotes.await()
            binding.progressBar.visibility = View.GONE
            adapterClass.setListUser(listUser)
            if (listUser.size == 0) {
                val parentLayout: View = binding.progressBar
                Snackbar.make(parentLayout, "No data found", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapterClass.itemUser)
    }

    override fun onResume() {
        super.onResume()
        loadDataAsync()
    }

}