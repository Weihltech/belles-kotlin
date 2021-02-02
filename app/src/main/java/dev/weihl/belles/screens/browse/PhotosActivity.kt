package dev.weihl.belles.screens.browse

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dev.weihl.belles.common.IntentKey
import dev.weihl.belles.common.loadImage
import dev.weihl.belles.data.BImage
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

        // album detail
        val photosJson = intent.getStringExtra(IntentKey.DETAIL)
        if (photosJson == null || photosJson.isEmpty()) {
            return finish()
        }
        val photoList = json2SexyImageList(photosJson)
        if (photoList.isEmpty()) {
            return finish()
        }

        // browse album
        initBrowseAlbum(photoList)

        // mask view
        initMaskView()
    }

    private fun initBrowseAlbum(photoList: List<BImage>) {
        recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        binding.root.postDelayed({
            binding.viewPager.adapter = PhotosAdapter(photoList)
            val index = intent.getIntExtra(IntentKey.INDEX, 0)
            binding.viewPager.setCurrentItem(index, false)
            Timber.d("setCurrentItem: $index")
        }, 500)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val adapter = binding.viewPager.adapter as PhotosAdapter
                val numTxt = "${binding.viewPager.currentItem + 1} · ${adapter.itemCount}"
                binding.tvNum.text = numTxt

                if (binding.tvNum.visibility != View.VISIBLE) {
                    binding.tvNum.visibility = View.VISIBLE
                }
            }
        })
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun initMaskView() {
        val originRect = intent.getParcelableExtra<Rect>(IntentKey.OBJECT_RECT) ?: return
        val originXY = intent.getIntArrayExtra(IntentKey.LOCATION)
        originXY?.let { itXY ->
            val originPhoto = MotionPhoto.OriginPhoto(
                itXY[0], itXY[1], originRect
            )
            runCatching {
                motionPhoto = MotionPhoto(
                    originPhoto, Handler(Looper.myLooper()!!),
                    motionPhotoCallBack
                )
                motionPhoto!!.maskOriginView(binding.root)?.let {
                    val referer = intent.getStringExtra(IntentKey.REFERER)
                    val url = intent.getStringExtra(IntentKey.URL)
                    if (referer != null && url != null) {
                        loadImage(it, referer, url)
                    }
                }
                Timber.d("MotionPhoto init $originPhoto")
            }
        }
    }

    private val motionPhotoCallBack = object : MotionPhoto.MotionCallBack {
        override fun maskViewRestoreFinish() {
            finish()
        }

        override fun maskViewAlphaChange(alpha: Float) {
            val tAlpha = 1 - alpha
            binding.viewPager.alpha = tAlpha
            binding.tvNum.alpha = tAlpha
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

