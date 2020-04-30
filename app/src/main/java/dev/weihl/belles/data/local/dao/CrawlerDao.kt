package dev.weihl.belles.data.local.dao

import androidx.room.*
import dev.weihl.belles.data.local.entity.Crawler

/**
 * 爬虫数据Dao
 *
 * @author Weihl Created by 2020/4/30
 *
 */
@Dao
interface CrawlerDao {

    @Insert
    fun insert(crawler: Crawler)

    @Delete
    fun delete(crawler: Crawler)

    @Update
    fun update(crawler: Crawler)

    @Query("SELECT * FROM crawler_table ct WHERE ct.tag =:tag ORDER BY ct.id")
    fun loadCrawlerList(tag: String): List<Crawler>

}