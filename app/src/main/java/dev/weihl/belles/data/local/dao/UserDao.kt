package dev.weihl.belles.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.weihl.belles.data.local.table.User

/**
 * @desc 用户信息数据表映射关系
 *
 * @author Weihl Created by 2019/12/6
 *
 */
@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user_table ORDER BY id DESC LIMIT 1")
    fun queryUser(): User

    @Query("DELETE FROM belles_table")
    fun deleteAll()


}