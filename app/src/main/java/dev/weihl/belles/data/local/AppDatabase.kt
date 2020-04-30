package dev.weihl.belles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.weihl.belles.data.local.dao.BellesDao
import dev.weihl.belles.data.local.dao.CrawlerDao
import dev.weihl.belles.data.local.dao.UserDao
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.local.entity.Crawler
import dev.weihl.belles.data.local.entity.User

/**
 * @desc Belles Data Base
 *
 * @author Weihl Created by 2019/11/28
 *
 */
@Database(entities = [
    Belles::class,
    User::class,
    Crawler::class],
    version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val bellesDao: BellesDao
    abstract val userDao: UserDao
    abstract val crawlerDao: CrawlerDao


    companion object {

        private const val DATA_BASE_NAME = "belles_db.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATA_BASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}