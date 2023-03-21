package com.example.notesapp.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notesapp.NoteApplication
import com.example.notesapp.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesWorker @Inject constructor(private val context: Context, params: WorkerParameters): Worker(context, params) {

        override fun doWork(): Result {
            val repository= (context as NoteApplication).repository
            Log.d("pp","worker called")
            CoroutineScope(Dispatchers.IO).launch {
                repository.createNotesBackground()

            }
            return Result.success()
    }
}