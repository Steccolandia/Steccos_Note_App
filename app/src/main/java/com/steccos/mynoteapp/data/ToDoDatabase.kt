package com.steccos.mynoteapp.data

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import com.steccos.mynoteapp.data.models.ToDoData
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract  fun toDoDao() : ToDoDao

    companion object {
       @Volatile
       private var INSTANCE: ToDoDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
       fun getDatabase(context: Context): ToDoDatabase =
           INSTANCE ?: synchronized(this) {
               INSTANCE
                   ?: buildDatabase(context).also { INSTANCE = it }
           }

       private fun buildDatabase(context: Context) =
           Room.databaseBuilder(
               context.applicationContext,
               ToDoDatabase::class.java, "todo_database"
           ).build()
   }


}