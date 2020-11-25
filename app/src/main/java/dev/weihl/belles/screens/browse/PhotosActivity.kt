package dev.weihl.belles.screens.browse

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
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


class PhotosActivity : BasicActivity() {

    // x, y
    private val centerPoint = arrayOf(0f, 0f)
    private lateinit var binding: ActivityPhotosBinding

    private lateinit var recyclerView: RecyclerView
    private var globalXY: IntArray? = null

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
            }
        })
        binding.btnBack.setOnClickListener { finish() }

        // 显示动画
        globalXY = intent.getIntArrayExtra("globalXY")
        Timber.d(globalXY?.contentToString())

        // finish 动画
    }

    private fun countCenterPoint() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
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

    private fun getCurrentItemViewBind(): ItemPhotosLayoutBinding? {
        try {
            return (recyclerView.getChildViewHolder(
                recyclerView.getChildAt(0)
            ) as PhotosAdapter.PhotosViewHolder).bind
        } catch (e: Exception) {
            // nothing
        }
        return null
    }

    private val touchMoveCallBack = object : TouchMoveCallBack {

        private var itemViewBind: ItemPhotosLayoutBinding? = null

        // x, y
        private val movePoint = arrayOf(0f, 0f)

        private fun countMoveRatio(downXY: Array<Float>, moveXY: Array<Float>) {
            val offsetY = (moveXY[1] - downXY[1]) * 0.0005f
            var moveRatio = 1 - offsetY
            moveRatio = if (moveRatio < 0) 0f else if (moveRatio > 1) 1f else moveRatio
            setMoveRatio(moveRatio)
        }

        private fun setMoveRatio(moveRatio: Float) {
            // refresh param
            binding.tvNum.alpha = moveRatio
            itemViewBind?.image?.setTag(R.id.value, moveRatio)
            itemViewBind?.image?.scaleX = moveRatio
            itemViewBind?.image?.scaleY = moveRatio
            itemViewBind?.imageBackground?.alpha = moveRatio
        }

        private fun countMovePoint(downXY: Array<Float>, moveXY: Array<Float>) {
            movePoint[0] = moveXY[0] - centerPoint[0]
            movePoint[1] = moveXY[1] - centerPoint[1]
            setMovePoint(movePoint)
        }

        private fun setMovePoint(point: Array<Float>) {
            itemViewBind?.image?.x = point[0]
            itemViewBind?.image?.y = point[1]
        }

        override fun onDown() {
            itemViewBind = getCurrentItemViewBind()
        }

        override fun onVerticalMove(downXY: Array<Float>, moveXY: Array<Float>) {
            if (itemViewBind == null) {
                return
            }
            countMoveRatio(downXY, moveXY)
            countMovePoint(downXY, moveXY)
        }

        override fun onUp() {
            val ratio = itemViewBind?.image?.getTag(R.id.value) as Float
            Timber.d("up . ratio $ratio ")
            if (ratio < 0.5f) {
                doAnimFinish()
                Timber.d("up . finish ")
            } else {
                setMoveRatio(1f)
                setMovePoint(arrayOf(0f, 0f))
            }
        }

        private fun doAnimFinish() {
            if (globalXY == null) {
                finish()
            } else {

                val targetX = globalXY!![0].minus(centerPoint[0] - 300)
                val targetY = globalXY!![1].minus(centerPoint[1] - 300)

                val x = itemViewBind?.image?.x
                Timber.d("x :>># $x")
                val xValueAnimator = x?.let {
                    ValueAnimator.ofFloat(it, targetX.toFloat())
                }
                xValueAnimator?.addUpdateListener {
                    val value = it.animatedValue as Float
                    itemViewBind?.image?.x = value
                    Timber.d("x :>>@ ${itemViewBind?.image?.x}")
                }
                xValueAnimator?.duration = 400
                xValueAnimator?.start()

                val y = itemViewBind?.image?.y
                Timber.d("y :>># $y")
                val yValueAnimator = y?.let {
                    ValueAnimator.ofFloat(it, targetY.toFloat())
                }
                yValueAnimator?.addUpdateListener {
                    itemViewBind?.image?.y = it.animatedValue as Float
                    Timber.d("y :>>@ ${itemViewBind?.image?.y}")
                }
                yValueAnimator?.duration = 400

                yValueAnimator?.start()

                binding.root.postDelayed({
                    finish()
                }, 300)
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }
}

