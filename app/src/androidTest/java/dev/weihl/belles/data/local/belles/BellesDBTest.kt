package dev.weihl.belles.data.local.belles

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

/**
 * @author Weihl Created by 2019/12/2
 */
class BellesDBTest {


    private lateinit var bellesDBDao: BellesDBDao
    private lateinit var bellesDB: BellesDB

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        bellesDB = Room.inMemoryDatabaseBuilder(context, BellesDB::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        bellesDBDao = bellesDB.bellesDBDao!!
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bellesDB.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = Belles()
        night.title = "1"
        bellesDBDao.insert(night)
        val belles = bellesDBDao.queryLastBelles()



        assertEquals(belles.title, "1")
    }
}