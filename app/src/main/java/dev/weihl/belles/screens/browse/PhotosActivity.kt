package dev.weihl.belles.screens.browse

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import dev.weihl.belles.MainApp
import dev.weihl.belles.R
import dev.weihl.belles.data.json2SexyImageList
import dev.weihl.belles.screens.BasicActivity
import kotlinx.android.synthetic.main.activity_photos.*
import timber.log.Timber
import kotlin.math.abs

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
                Toast.makeText(MainApp.getAppContext(), "AAAA", Toast.LENGTH_LONG).show()
            }
        }
    }


    private val eventDown = arrayOf(0f, 0f)
    private val eventMove = arrayOf(0f, 0f)
    private var exitAction = false
    private var hasCount = false
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                eventDown[0] = event.x
                eventDown[1] = event.y
                Timber.d("ACTION_DOWN ")
                return false
            }
            MotionEvent.ACTION_UP -> {
                exitAction = false
                hasCount = false
                Timber.d("ACTION_UP   : exitAction = $exitAction ; hasCount = $hasCount")
            }
            MotionEvent.ACTION_MOVE -> {
                if (!hasCount) {
                    eventMove[0] = event.x
                    eventMove[1] = event.y

                    val offsetX = abs(eventDown[0] - eventMove[0])
                    val offsetY = abs(eventDown[1] - eventMove[1])
                    exitAction = offsetY - offsetX > 0
                    hasCount = true

                    Timber.d("ACTION_MOVE   : exitAction,hasCount = $exitAction,$hasCount")
                }
            }
        }

        if (hasCount && exitAction) {
            Toast.makeText(MainApp.getAppContext(), "移动图片", Toast.LENGTH_LONG).show()
            return true
        }

        return view_pager.dispatchTouchEvent(event)
    }

    override fun onBackPressed() {
        finish()
    }
}
