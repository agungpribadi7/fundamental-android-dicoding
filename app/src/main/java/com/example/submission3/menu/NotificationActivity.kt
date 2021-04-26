package com.example.submission3.menu

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.submission3.R
import com.example.submission3.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotificationBinding
    private lateinit var mSharedPreferences: SharedPreferences
    companion object {
        const val PREFERENCES = "PREFERENCES"
        private const val DAILY = "DAILY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mSharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val switchValue = mSharedPreferences.getBoolean(DAILY, false)
        binding.switchReminder.isChecked = switchValue
        val alarmReceiver = AlarmReceiver()
        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setReminder(
                    this,
                    getString(R.string.daily_message)
                )
            } else {
                alarmReceiver.cancelAlarm(this)
            }
            mSharedPreferences.edit().putBoolean(DAILY, isChecked).apply()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}