package dev.weihl.belles.screens.browse

import android.os.Bundle
import android.view.MotionEvent
import dev.weihl.belles.R
import dev.weihl.belles.data.json2SexyImageList
import dev.weihl.belles.screens.BasicActivity
import kotlinx.android.synthetic.main.activity_photos.*
import timber.log.Timber

class PhotosActivity : BasicActivity() {

    init {
        Timber.tag("PhotosActivity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        val photosJson = intent.getStringExtra("details")
        if (photosJson == null || photosJson.isEmpty()) {
            finish()
            return
        }

        val photoList = json2SexyImageList(photosJson)
        if (photoList == null || photoList.isEmpty()) {
            finish()
            return
        }

        view_pager.adapter = PhotosAdapter(photoList, photosAdapterCallBack())

        btn_back.setOnClickListener { finish() }
    }

    private fun photosAdapterCallBack(): PhotosAdapterCallBack {
        return object : PhotosAdapterCallBack {
            override fun itemClick() {
                // Nothing
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return judgeTouchEvent(event) || super.dispatchTouchEvent(event)
    }

    override fun onBackPressed() {
        finish()
    }
}
