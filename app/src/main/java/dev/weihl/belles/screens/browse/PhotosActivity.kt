package dev.weihl.belles.screens.browse

import android.os.Bundle
import android.view.MotionEvent
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
                // Nothing
            }
        }
    }


    private val eventDown = arrayOf(0f, 0f)
    private val eventMove = arrayOf(0f, 0f)
    private var verticalSliding = false
    private var hasJudgeSliding = false
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                eventDown[0] = event.x
                eventDown[1] = event.y
                Timber.d("ACTION_DOWN ${eventDown.contentToString()}")
            }
            MotionEvent.ACTION_UP -> {
                hasJudgeSliding = false
                verticalSliding = false
                Timber.d("ACTION_UP   ")
            }
            MotionEvent.ACTION_MOVE -> {
                eventMove[0] = event.x
                eventMove[1] = event.y

                if (!hasJudgeSliding) {
                    val offsetX = abs(eventDown[0] - eventMove[0])
                    val offsetY = abs(eventDown[1] - eventMove[1])

                    Timber.d("ACTION_MOVE ${eventMove.contentToString()} ； [ offsetX：$offsetX , offsetY:$offsetY ]")
                    // 单位计算 >10
                    if (offsetX > 10 || offsetY > 10) {
                        hasJudgeSliding = true
                        if (offsetY - offsetX > 2) {
                            // 此时，垂直滑动，角度 大于 45°；
                            Timber.d("ACTION_MOVE  is 垂直滑动 !")
                            verticalSliding = true
                        }
                    }
                }

            }
        }

        if (hasJudgeSliding && verticalSliding) {
            Timber.d("ACTION_MOVE(垂直控制) :${eventMove.contentToString()}")
            return true
        }

        return super.dispatchTouchEvent(event)
    }

    override fun onBackPressed() {
        finish()
    }
}
