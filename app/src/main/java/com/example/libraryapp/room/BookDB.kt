package com.example.libraryapp.room

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BooksDB : RoomDatabase(){
    abstract fun bookDao(): BookDAO

    //companion object
    companion object{
        @Volatile
        private var INSTANCE: BooksDB? = null

        fun getInstance(context: Context): BooksDB{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BooksDB::class.java,
                        "books_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}