package com.example.submission3

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission3.databinding.ActivityProfileBinding
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.AVATAR
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.COMPANY
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.IS_FAVORITE
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.LOCATION
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.NAME
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.N_FOLLOWER
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.N_FOLLOWING
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.REPOSITORY
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.USERNAME
import com.example.submission3.db.FavoriteHelper
import com.example.submission3.favourite.FavoriteActivity
import com.example.submission3.menu.NotificationActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var cekFav : String
    private lateinit var dbHelper : FavoriteHelper
    private lateinit var curUser : User
    companion object{
        const val get_data = "GET_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = FavoriteHelper.getInstance(applicationContext)
        dbHelper.open()
        curUser = intent.getParcelableExtra<User>(get_data) as User
        supportActionBar?.title = curUser.username
        cekFav = curUser.isFavourite.toString()
        if(cekFav == "1"){
            binding.imgFavorite.setImageResource(R.drawable.heartjadi)
        }
        binding.imgFavorite.setOnClickListener(this)

        Glide.with(applicationContext)
            .load(curUser.img)
            .apply{ RequestOptions().override(55, 55)}
            .into(binding.imgProfile)

        binding.tvName.text = curUser.name
        binding.tvFollower.text = curUser.follower.toString()
        binding.tvFollowing.text = curUser.following.toString()
        binding.tvCompany.text = curUser.company
        binding.tvLocation.text = curUser.location
        binding.tvRepository.text = curUser.repository.toString()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.setMainUser(curUser)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.img_favorite) {
            if(cekFav == "1"){
                val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + curUser.username)

                contentResolver.delete(uriWithId, null, null)
                dbHelper.deleteById(curUser.username.toString())
                Snackbar.make(view, R.string.unsubscribe,  Snackbar.LENGTH_LONG).show()
                binding.imgFavorite.setImageResource(R.drawable.hearttransparan)
                cekFav = "0"
            } else {
                val nama = curUser.name
                val username = curUser.username
                val img = curUser.img
                val company = curUser.company
                val location = curUser.location
                val repository = curUser.repository
                val nFollower = curUser.follower
                val nFollowing = curUser.following
                cekFav = "1"

                val values = ContentValues()
                values.put(NAME, nama)
                values.put(USERNAME, username)
                values.put(AVATAR, img)
                values.put(COMPANY, company)
                values.put(LOCATION, location)
                values.put(REPOSITORY, repository)
                values.put(IS_FAVORITE, cekFav)
                values.put(N_FOLLOWER, nFollower)
                values.put(N_FOLLOWING, nFollowing)

                contentResolver.insert(CONTENT_URI, values)
                Snackbar.make(view, R.string.subscribe,  Snackbar.LENGTH_LONG).show()
                binding.imgFavorite.setImageResource(R.drawable.heartjadi)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_child, menu)
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
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}