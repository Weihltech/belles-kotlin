package dev.weihl.belles.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @desc Belles SQLite Entity
 *
 * @author Weihl Created by 2019/11/28
 *
 */
@Entity(tableName = "belles_table")
data class Belles(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "BellesDB_Name",

    @ColumnInfo(name = "href")
    var href: String,

    @ColumnInfo(name = "thumb")
    var thumb: String,

    @ColumnInfo(name = "thumbWh")
    var thumbWh: String,

    @ColumnInfo(name = "tab")
    var tab: String,// qingchun,xinggan,xiaoyuan

    @ColumnInfo(name = "favorite")
    var favorite: String,// yes or no

    @ColumnInfo(name = "details")
    var details: String,// json

    @ColumnInfo(name = "date")
    var date: Long,

    @ColumnInfo(name = "referer")
    var referer: String

)