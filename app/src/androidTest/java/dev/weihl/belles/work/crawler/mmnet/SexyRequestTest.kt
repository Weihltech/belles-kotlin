package dev.weihl.belles.work.crawler.mmnet

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.SexyRequest
import org.junit.Before
import org.junit.Test

class SexyRequestTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun load_group_page_list() {
        val sexyCrawler = SexyRequest(context)
        assert(sexyCrawler.loadGroupPageList(2).isNotEmpty())
    }

    @Test
    fun load_page_detail_list() {
        val sexyCrawler = SexyRequest(context)
        val pageUrl = "https://www.mm131.net/xinggan/5689.html";
        assert(sexyCrawler.loadPageDetailList(pageUrl).isNotEmpty())
    }

    @Test
    fun load() {
        val sexyCrawler = SexyRequest(context)
        sexyCrawler.load(1, object : SexyRequest.CallBack {
            override fun result(bellesList: ArrayList<Belles>) {
                TODO("Not yet implemented")
            }

        })
    }

}