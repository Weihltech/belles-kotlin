package dev.weihl.belles.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @desc 爬虫记录
 *
 * @author Weihl Created by 2020/4/30
 *
 */
@Entity(tableName = "crawler_table")
class Crawler {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    // 标记
    @ColumnInfo(name = "tag")
    var tag: String = ""

    // 当前标记记录
    @ColumnInfo(name = "content")
    var content: String = ""

}