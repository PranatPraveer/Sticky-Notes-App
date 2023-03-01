package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build

import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import android.provider.Settings
import androidx.annotation.RequiresApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = applicationContext
        val settingsCanWrite = hasWriteSettingsPermission(context)

        if (!settingsCanWrite) {
            changeWriteSettingsPermission(context)
        } else {
            changeScreenBrightness(context, 255)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasWriteSettingsPermission(context: Context): Boolean {
        var ret = true
        // Get the result from below code.
        ret = Settings.System.canWrite(context)
        return ret
    }

    // Start can modify system settings panel to let user change the write
    // settings permission.
    private fun changeWriteSettingsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        startActivity(intent)
    }


    private fun changeScreenBrightness(
        context: Context,
        screenBrightnessValue: Int
    ) {   // Change the screen brightness change mode to manual.
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
        // Apply the screen brightness value to the system, this will change
        // the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(
            context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
        )
    }
}