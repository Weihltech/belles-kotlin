package dev.weihl.belles.data.local.belles

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dev.weihl.belles.data.local.AppDatabase
import dev.weihl.belles.data.local.dao.BellesDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * @author Weihl Created by 2019/12/2
 */
class BellesDBTest {


    private lateinit var bellesDao: BellesDao
    private lateinit var bellesDB: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        bellesDB = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        bellesDao = bellesDB.bellesDao as BellesDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bellesDB.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
//        val night = Belles()
//        bellesDao.insert(night)
//        val belles = bellesDao.queryLastBelles()
//        assertEquals(belles.title, "1")
    }
}