package com.example.drawingapp

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Database(entities = [DrawData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DrawDatabase : RoomDatabase() {
    abstract fun drawDao(): DrawDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DrawDatabase? = null

        //When we want a DB we call this (basically static) method
        //val theDB = WeatherDatabase.getDatabase(myContext)
        fun getDatabase(context: Context): DrawDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                //if another thread initialized this before we got the lock
                //return the object they created
                if (INSTANCE != null) return INSTANCE!!
                //otherwise we're the first thread here, so create the DB
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrawDatabase::class.java,
                    "drawing_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}


@Dao
interface DrawDAO {
    @Insert
    suspend fun addPictData(data: DrawData)

    @Update
    suspend fun updatePictData(data: DrawData)

    // Get drawing by name
    @Query("SELECT * FROM drawings WHERE name = :name LIMIT 1")
    fun getDrawingByName(name: String): Flow<DrawData?>

    // Check if the image is already in database
    @Query("SELECT EXISTS(SELECT 1 FROM drawings WHERE name = :name LIMIT 1)")
    suspend fun checkDrawingInDatabase(name: String): Boolean

    // Delete image from data base
    @Query("DELETE FROM drawings WHERE id = :drawingId")
    suspend fun deletePictDataById(drawingId: Int)

    // Get all drawings
    @Query("SELECT * FROM drawings ORDER BY id DESC")
    fun getAllDrawings(): Flow<List<DrawData>>

    // Get drawing by ID
    @Query("SELECT * FROM drawings WHERE id = :id LIMIT 1")
    fun getDrawingById(id: Int): Flow<DrawData>
}
