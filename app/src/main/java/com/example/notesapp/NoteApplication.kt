package com.example.notesapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.notesapp.repository.NoteRepository
import com.example.notesapp.worker.NotesWorker
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NoteApplication :Application(){

    @Inject
    lateinit var repository: NoteRepository
    override fun onCreate() {
        super.onCreate()
        setUpWorker()
    }

    private fun setUpWorker() {
        val constraint= Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest= PeriodicWorkRequest.Builder(NotesWorker::class.java,15, TimeUnit.MINUTES).setConstraints(constraint).build()
    WorkManager.getInstance(this).enqueue(workerRequest)
    }
}