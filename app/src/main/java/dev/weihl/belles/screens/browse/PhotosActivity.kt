package dev.weihl.belles.screens.browse

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dev.weihl.belles.databinding.ActivityPhotosBinding
import dev.weihl.belles.databinding.ItemPhotosLayoutBinding
import dev.weihl.belles.json2SexyImageList
import dev.weihl.belles.screens.BasicActivity
import timber.log.Timber


class PhotosActivity : BasicActivity() {

    private lateinit var binding: ActivityPhotosBinding
    private lateinit var recyclerView: RecyclerView
    private var motionPhoto: MotionPhoto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photosJson = intent.getStringExtra("details")
        if (photosJson == null || photosJson.isEmpty()) {
            return finish()
        }

        val photoList = json2SexyImageList(photosJson)
        if (photoList.isEmpty()) {
            return finish()
        }

//        recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
//        binding.viewPager.adapter = PhotosAdapter(photoList)
//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                val adapter = binding.viewPager.adapter as PhotosAdapter
//                val numTxt = "${binding.viewPager.currentItem + 1} · ${adapter.itemCount}"
//                binding.tvNum.text = numTxt
//
//                getCurrentItemViewBind()?.let {
//                    Timber.d("MotionPhoto holdOriginView ")
//                    motionPhoto?.holdOriginView()
//                }
//            }
//        })
//        binding.btnBack.setOnClickListener { finish() }

        initPhotoParamData()
    }

    private fun initPhotoParamData() {

        val simple = intent.getBooleanExtra("simple", false)
        if (simple) {
            return
        }

        val originRect = intent.getParcelableExtra("globalRect") as Rect
        val originXY = intent.getIntArrayExtra("globalXY")
        originXY?.let { itXY ->
            val originPhoto = MotionPhoto.OriginPhoto(
                itXY[0], itXY[1], originRect
            )
            motionPhoto = MotionPhoto(originPhoto, Handler())
            binding.root.addView(motionPhoto!!.maskView)
            motionPhoto?.holdOriginView()
            Timber.d("MotionPhoto init $originPhoto")
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        if (isScalePhotos()) {
            return super.dispatchTouchEvent(event)
        }

        return motionPhoto?.holdTouchEvent(event) == true || super.dispatchTouchEvent(event)
    }

    private fun isScalePhotos(): Boolean {
        val bind = getCurrentItemViewBind()
        return bind?.image?.scale != bind?.image?.minimumScale
    }

    private fun getCurrentItemViewBind(): ItemPhotosLayoutBinding? {
        runCatching {
            return (recyclerView.getChildViewHolder(
                recyclerView.getChildAt(0)
            ) as PhotosAdapter.PhotosViewHolder).bind
        }
        return null
    }

    override fun onBackPressed() {
        finish()
    }

}

