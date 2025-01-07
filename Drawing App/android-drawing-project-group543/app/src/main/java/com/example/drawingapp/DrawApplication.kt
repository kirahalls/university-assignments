package com.example.drawingapp

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DrawApplication : Application() {

    //coroutine scope tied to the application lifetime which we can run suspend functions in
    val scope = CoroutineScope(SupervisorJob())

    //get a reference to the DB singleton
    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            DrawDatabase::class.java,
            "drawing_database"
        ).build()
    }

    //create our repository using lazy to access the DB when we need it
    val drawRepository by lazy { DrawRepository(scope, db.drawDao()) }
}