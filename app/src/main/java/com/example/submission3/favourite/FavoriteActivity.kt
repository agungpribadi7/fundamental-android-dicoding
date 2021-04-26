package com.example.submission3.favourite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.custom.CustomOnClickListener
import com.example.submission3.menu.NotificationActivity
import com.example.submission3.ProfileActivity
import com.example.submission3.User
import com.example.submission3.databinding.ActivityFavoriteBinding
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.example.submission3.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(), CustomOnClickListener {
    private lateinit var binding : ActivityFavoriteBinding
    private var listUser = ArrayList<User>()
    private lateinit var  adapterClass : FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Favorite"
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        adapterClass = FavoriteAdapter(this)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(com.example.submission3.R.menu.menu_child, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            com.example.submission3.R.id.menu_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            com.example.submission3.R.id.menu_favorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
            com.example.submission3.R.id.menu_notification -> {
                val mIntent = Intent(this, NotificationActivity::class.java)
                startActivity(mIntent)
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(user: User) {
        val intent = Intent(this@FavoriteActivity, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.get_data, user)
        startActivity(intent)
    }

}