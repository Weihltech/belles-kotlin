package dev.weihl.belles.screens.about

import android.os.Bundle
import dev.weihl.belles.R
import dev.weihl.belles.screens.BasicActivity

class AboutActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

//        appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{ _, verticalOffset: Int ->
//            val seekPosition = -verticalOffset / appbarLayout.totalScrollRange.toFloat()
//            appbarMotionLayout.progress = seekPosition
//        })
    }
}
