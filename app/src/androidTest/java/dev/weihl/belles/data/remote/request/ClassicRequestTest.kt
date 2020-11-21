package dev.weihl.belles.data.remote.request

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dev.weihl.belles.data.BellesPage
import junit.framework.TestCase
import org.junit.Before

/**
 * Des:
 *
 * @author Weihl
 * Created 2020/11/20
 */
class ClassicRequestTest : TestCase() {

    private lateinit var context: Context

    @Before
    override fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }


    fun testLoadPageList() {

        val classicRequest = ClassicRequest(context)
        assert(classicRequest.loadPageList(2).isNotEmpty())

    }

    fun testLoadPageImageList() {
        val url = "https://www.tupianzj.com/meinv/20200309/205965.html"
        val bellesPage = BellesPage("", url, "")
        val classicRequest = ClassicRequest(context)
        classicRequest.loadPageImageList(bellesPage)


    }
}