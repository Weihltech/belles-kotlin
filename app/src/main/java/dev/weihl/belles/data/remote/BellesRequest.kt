package dev.weihl.belles.data.remote

import androidx.annotation.NonNull
import dev.weihl.belles.data.BellesImage
import dev.weihl.belles.data.BellesPage

/**
 * Des:
 * 请求类接口
 *
 * @author Weihl
 * Created 2020/11/20
 */
abstract class BellesRequest() {

    abstract fun getPageUrl(page: Int): String

    abstract fun loadPageList(page: Int): ArrayList<BellesPage>

    abstract fun loadPageImageList(@NonNull bellesPage: BellesPage): ArrayList<BellesImage>
}