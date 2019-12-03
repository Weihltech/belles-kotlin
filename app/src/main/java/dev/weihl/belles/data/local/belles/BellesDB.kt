package dev.weihl.belles.data.local.belles

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.weihl.belles.data.local.LocalRepository

/**
 * @desc Belles Data Base
 *
 * @author Weihl Created by 2019/11/28
 *
 */
@Database(entities = [Belles::class], version = 1, exportSchema = false)
abstract class BellesDB : RoomDatabase() {

    abstract val bellesDBDao: BellesDBDao

    companion object {

        @Volatile
        private var INSTANCT: BellesDB? = null

        fun getInstance(context: Context): BellesDB {
            synchronized(this) {
                var instance = INSTANCT
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BellesDB::class.java,
                        LocalRepository.DATA_BASE_NAME
                    )
                        .fallbackToDestructiveMigration().build()
                    INSTANCT = instance
                }
                return instance
            }
        }
    }

}