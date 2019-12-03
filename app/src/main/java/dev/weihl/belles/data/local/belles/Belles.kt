package dev.weihl.belles.data.local.belles

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

    @ColumnInfo(name = "desc")
    var desc: String = "BellesDB_Desc",

    @ColumnInfo(name = "url")
    var url: String = "BellesDB_Url"



)