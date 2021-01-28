package dev.weihl.belles.screens.browse

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dev.weihl.belles.databinding.ActivityPhotosBinding
import dev.weihl.belles.databinding.ItemPhotosLayoutBinding
import dev.weihl.belles.json2SexyImageList
import dev.weihl.belles.screens.BasicActivity


class PhotosActivity : BasicActivity(), PhotosActionCallBack {

    private lateinit var binding: ActivityPhotosBinding
    private lateinit var recyclerView: RecyclerView
    private var markPhotoEnlargeAnim: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPhotoParamData()

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

        recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        binding.viewPager.adapter = PhotosAdapter(photoList)
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

        // start anim
    }

    private fun initPhotoParamData() {

        val simple = intent.getBooleanExtra("simple", false)
        if (simple) {
            return
        }

        // 全屏
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        centerPoint[0] = (width / 2).toFloat()
        centerPoint[1] = (height / 2).toFloat()
        pxiWidth = centerPoint[0] * 2
        pxiHeight = centerPoint[1] * 2

        // 图片参数 数据
        photoGlobalXY = intent.getIntArrayExtra("globalXY")
        photoGlobalX = photoGlobalXY?.get(0)?.toFloat()!!
        photoGlobalY = photoGlobalXY?.get(1)?.toFloat()!!

        photoGlobalRect = intent.getParcelableExtra("globalRect")
        photoGlobalWidth = photoGlobalRect?.width()?.plus(30)!!
        photoGlobalHeight = photoGlobalRect?.height()?.plus(30)!!
        actionCallBack = this
    }

//    private fun photosAdapterCallBack(): PhotosAdapterCallBack {
//        return object : PhotosAdapterCallBack {
//            override fun photoOutsideClick() {
//                if (!isScalePhotos()) {
//                    finish()
//                }
//            }
//        }
//    }

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

