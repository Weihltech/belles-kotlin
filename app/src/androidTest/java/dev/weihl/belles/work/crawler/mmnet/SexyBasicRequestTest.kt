package dev.weihl.belles.work.crawler.mmnet

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test

class SexyBellesRequestTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun load_group_page_list() {
        val sexyCrawler =
            SexyRequest(context)
        assert(sexyCrawler.loadPageList(2).isNotEmpty())
    }

    @Test
    fun load_page_detail_list() {
        val sexyCrawler =
            SexyRequest(context)
        val pageUrl = "https://www.mm131.net/xinggan/5689.html";
    }

    @Test
    fun load() {

    }

}