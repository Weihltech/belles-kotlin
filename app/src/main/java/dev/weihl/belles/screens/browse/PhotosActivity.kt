package dev.weihl.belles.screens.browse

import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.weihl.belles.MainApp
import dev.weihl.belles.R
import dev.weihl.belles.screens.BasicActivity
import dev.weihl.belles.work.bean.WorkExtraImg
import kotlinx.android.synthetic.main.activity_photos.*

class PhotosActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        val photosJson = intent.getStringExtra("details")
        val photoList: List<WorkExtraImg> = Gson().fromJson(
            photosJson,
            object : TypeToken<List<WorkExtraImg?>?>() {}.type
        )

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
}
