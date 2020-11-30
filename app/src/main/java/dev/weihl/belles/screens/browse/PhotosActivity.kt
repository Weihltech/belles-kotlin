package dev.weihl.belles.screens.browse

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dev.weihl.belles.R
import dev.weihl.belles.data.json2SexyImageList
import dev.weihl.belles.databinding.ActivityPhotosBinding
import dev.weihl.belles.databinding.ItemPhotosLayoutBinding
import dev.weihl.belles.screens.BasicActivity
import timber.log.Timber


class PhotosActivity : BasicActivity(), PhotosActionCallBack {

    private lateinit var binding: ActivityPhotosBinding

    private lateinit var recyclerView: RecyclerView

    var markPhotoEnlargeAnim: Boolean = true

    init {
        Timber.tag("PhotosActivity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countCenterPoint()

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

                if (markPhotoEnlargeAnim) {
                    markPhotoEnlargeAnim = false
                    photoEnlargeAnim()
                }
            }
        })
        binding.btnBack.setOnClickListener { finish() }

        photoGlobalXY = intent.getIntArrayExtra("globalXY")
        photoGlobalRect = intent.getParcelableExtra("globalRect")
        actionCallBack = this

        // start anim

    }

    private fun countCenterPoint() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        centerPoint[0] = (width / 2).toFloat()
        centerPoint[1] = (height / 2).toFloat()
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
        val bind = getCurrentItemViewBind()
        return bind?.image?.scale != bind?.image?.minimumScale
    }

    override fun getCurrentItemViewBind(): ItemPhotosLayoutBinding? {
        try {
            return (recyclerView.getChildViewHolder(
                recyclerView.getChildAt(0)
            ) as PhotosAdapter.PhotosViewHolder).bind
        } catch (e: Exception) {
            // nothing
        }
        return null
    }

    override fun getIndicatorView(): View {
        return binding.tvNum
    }

    override fun getHandler(): Handler {
        return binding.root.handler
    }

    override fun onBackPressed() {
        finish()
    }

}

