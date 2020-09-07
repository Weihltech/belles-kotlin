package dev.weihl.belles.screens.home.favorite


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import dev.weihl.belles.R
import dev.weihl.belles.databinding.FragmentCollectBinding
import dev.weihl.belles.screens.BasicFragment


class FavoriteFragment : BasicFragment() {

    private lateinit var binding: FragmentCollectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_collect, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {

            val glideUrl = GlideUrl(
                "https://img1.mmmw.net/pic/5262/39.jpg",
                LazyHeaders.Builder()
                    .addHeader("Referer", "https://www.mm131.net/xinggan/5262_39.html").build()
            )


            Glide.with(it).load(glideUrl).thumbnail().into(binding.imageView)

        }


    }

}
