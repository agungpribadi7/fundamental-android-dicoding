package com.example.submission3.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.User
import com.example.submission3.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowerFragment : Fragment() {
    private lateinit var binding : FragmentFollowerBinding
    private lateinit var adapter : FollowerAdapter
    private var listUser = ArrayList<User>()
    companion object {
        private const val ARG_USER = "section_user"
        @JvmStatic
        fun newInstance(user : User) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user : User? = arguments?.getParcelable(ARG_USER)
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 24372cf649be896b10eca81777ed029e6df3c2e8")
        val url = "https://api.github.com/users/${user?.username}/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBarFollower.visibility = View.GONE
                val result = String(responseBody)
                try {
                    listUser.clear()
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getDetail(username)
                    }

                } catch (e: Exception) {
                    Toast.makeText(getView()?.context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBarFollower.visibility = View.GONE
                Toast.makeText(getView()?.context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        showRecycleView()
    }

    private fun getDetail(username: String) {
        binding.progressBarFollower.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 24372cf649be896b10eca81777ed029e6df3c2e8")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBarFollower.visibility = View.GONE
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
                    Toast.makeText(view?.context, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBarFollower.visibility = View.GONE
                val errorMessage = error.message
                Toast.makeText(view?.context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showRecycleView(){
        binding.rvMainFollower.layoutManager = LinearLayoutManager(activity)
        adapter = FollowerAdapter(listUser)
        binding.rvMainFollower.adapter = adapter
    }


}