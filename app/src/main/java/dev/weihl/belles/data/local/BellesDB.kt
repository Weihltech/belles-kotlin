package dev.weihl.belles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.weihl.belles.data.local.dao.BellesDao
import dev.weihl.belles.data.local.dao.UserDao
import dev.weihl.belles.data.local.table.Belles
import dev.weihl.belles.data.local.table.User

/**
 * @desc Belles Data Base
 *
 * @author Weihl Created by 2019/11/28
 *
 */
@Database(entities = [Belles::class, User::class], version = 1, exportSchema = false)
abstract class BellesDB : RoomDatabase() {

    abstract val bellesDao: BellesDao
    abstract val userDao: UserDao

    companion object {

        private const val DATA_BASE_NAME = "belles_db.db"

        @Volatile
        private var INSTANCT: BellesDB? = null

        fun getInstance(context: Context): BellesDB {
            synchronized(this) {
                var instance = INSTANCT
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BellesDB::class.java,
                        DATA_BASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCT = instance
                }
                return instance
            }
        }
    }

}