package com.android.abtesting

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3)
            .build()

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    displayScreen()
                }
            }

    }

    private fun displayScreen() {
        val textView = findViewById<TextView>(R.id.textView)

        val experiment = firebaseRemoteConfig.getString("text_bg_change")
        textView.setBackgroundColor(Color.parseColor("#$experiment"))
    }
}