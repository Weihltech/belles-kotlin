package dev.weihl.belles.data.local.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @desc 用户信息
 *
 * @author Weihl Created by 2019/12/6
 *
 */
@Entity(tableName = "user_table")
data class User(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "gender")
    var gender: String
)