package dev.weihl.belles.screens.browse

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import dev.weihl.belles.MainApp
import dev.weihl.belles.R
import dev.weihl.belles.data.json2SexyImageList
import dev.weihl.belles.screens.BasicActivity
import kotlinx.android.synthetic.main.activity_photos.*

class PhotosActivity : BasicActivity() {

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
                Toast.makeText(MainApp.getAppContext(), "AAAA", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return view_pager.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}
