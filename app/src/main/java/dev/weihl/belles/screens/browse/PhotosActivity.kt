package dev.weihl.belles.screens.browse

import android.os.Bundle
import android.view.MotionEvent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dev.weihl.belles.R
import dev.weihl.belles.data.json2SexyImageList
import dev.weihl.belles.databinding.ActivityPhotosBinding
import dev.weihl.belles.screens.BasicActivity
import timber.log.Timber

class PhotosActivity : BasicActivity() {

    private lateinit var binding: ActivityPhotosBinding

    private lateinit var recyclerView: RecyclerView

    init {
        Timber.tag("PhotosActivity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_photos, null, false
        )
        setContentView(binding.root)

        val photosJson = intent.getStringExtra("details")
        if (photosJson == null || photosJson.isEmpty()) {
            finish()
            return
        }

        val photoList = json2SexyImageList(photosJson)
        if (photoList == null || photoList.isEmpty()) {
            return finish()

        }

        recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        binding.viewPager.adapter = PhotosAdapter(photoList, photosAdapterCallBack())
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val adapter = binding.viewPager.adapter as PhotosAdapter
                val numTxt = "${binding.viewPager.currentItem + 1} · ${adapter.itemCount}"
                binding.tvNum.text = numTxt
            }
        })
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun photosAdapterCallBack(): PhotosAdapterCallBack {
        return object : PhotosAdapterCallBack {
            override fun photoOutsideClick() {
                finish()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        if (isScalePhotos()) {
            return super.dispatchTouchEvent(event)
        }

        return judgeTouchEvent(event, touchMoveCallBack) || super.dispatchTouchEvent(event)
    }

    private fun isScalePhotos(): Boolean {
        val viewHolder = getCurrentViewHolder()
        return viewHolder.bind.image.scale != viewHolder.bind.image.minimumScale
    }

    private fun getCurrentViewHolder(): PhotosAdapter.PhotosViewHolder {
        return recyclerView.getChildViewHolder(
            recyclerView.getChildAt(0)
        ) as PhotosAdapter.PhotosViewHolder
    }

    private val touchMoveCallBack = object : TouchMoveCallBack {

        private var alpha = 1f

        init {
            Timber.tag("touchMoveCallBack")
        }

        override fun onDown() {
            alpha = 1f
        }

        override fun onVerticalMove(downXY: Array<Float>, moveXY: Array<Float>) {
            val offsetY = (moveXY[1] - downXY[1]) * 0.001f
            alpha -= offsetY
            if (alpha < 0) {
                alpha = 0f
            } else if (alpha > 1) {
                alpha = 1f
            }
            Timber.d("move . alpha $alpha ; offsetY $offsetY")

            doTouchMoveAction()
        }

        override fun onUp() {
            Timber.d("up . alpha $alpha ")
            if (alpha < 0.4f) {
                finish()
                Timber.d("up . finish ")
            } else {
                alpha = 1.0f
                doTouchMoveAction()
            }
        }

        private fun doTouchMoveAction() {
            val viewHolder = getCurrentViewHolder()
            viewHolder.bind.imageLayout.alpha = alpha
            viewHolder.bind.image.scaleX = alpha
            viewHolder.bind.image.scaleY = alpha
            binding.tvNum.alpha = alpha
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
