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

    }
}