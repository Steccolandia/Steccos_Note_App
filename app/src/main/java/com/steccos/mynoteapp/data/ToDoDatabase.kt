package com.steccos.mynoteapp.data

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import com.steccos.mynoteapp.data.models.ToDoData
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

//exportschema is set to false in the course
@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract  fun toDoDao() : ToDoDao
    /*
//next block was written during the course, the actual one comes from the github file
    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null
        //the annotation in the next line I added it as without it, the synchronized was giving an error
        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context : Context) : ToDoDatabase? {

            val tempInstance = INSTANCE
            if (INSTANCE != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, ToDoDatabase::class.java,
                "todo_database").build()
                INSTANCE = instance
                return instance
            }
        }

    }*/
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