package dev.weihl.belles.data.remote.request

import android.content.Context
import dev.weihl.belles.data.BellesImage
import dev.weihl.belles.data.BellesPage
import dev.weihl.belles.data.remote.BellesRequest

/**
 * Des:
 * 古典服装
 *
 * @author Weihl
 * Created 2020/11/20
 */

class ClassicRequest(context: Context) : BellesRequest() {

    override fun getPageUrl(page: Int): String {
        TODO("Not yet implemented")
    }

    override fun loadPageList(page: Int): ArrayList<BellesPage> {
        TODO("Not yet implemented")
    }

    override fun loadPageImageList(bellesPage: BellesPage): ArrayList<BellesImage> {
        TODO("Not yet implemented")
    }


}