package com.android.abtesting

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
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

        val token = FirebaseInstanceId.getInstance().token

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
        val message = firebaseRemoteConfig.getString(REMOTE_CONFIG_KEY.WELCOME_MESSAGE.value)
        textView.isAllCaps =
            firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY.WELCOME_MESSAGE_CAPS.value)

        textView.text = message
    }
}

enum class REMOTE_CONFIG_KEY(val value: String) {
    WELCOME_MESSAGE_CAPS("welcome_message_caps"),
    WELCOME_MESSAGE("welcome_message")
}