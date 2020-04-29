package dev.weihl.belles.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.weihl.belles.data.local.entity.Belles

/**
 * @desc DebllesDB 数据访问对象
 *
 * @author Weihl Created by 2019/11/28
 *
 */
@Dao
interface BellesDao {

    @Insert
    fun insert(belles: Belles)

    @Update
    fun update(belles: Belles)

    @Delete
    fun delete(belles: Belles)

    @Query("DELETE FROM belles_table")
    fun deleteAll()

    @Query("DELETE FROM belles_table WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM belles_table")
    fun queryAll(): LiveData<List<Belles>>

    @Query("SELECT * FROM belles_table ORDER BY id DESC")
    fun queryAllDescId(): LiveData<List<Belles>>

    @Query("SELECT * FROM belles_table ORDER BY id DESC LIMIT 1")
    fun queryMaxId(): Belles

    @Query("SELECT * FROM belles_table WHERE id = :id")
    fun queryById(id: Long): Belles

    @Query("SELECT * FROM belles_table  ORDER BY id DESC LIMIT 1")
    fun queryLastBelles(): LiveData<Belles>

}