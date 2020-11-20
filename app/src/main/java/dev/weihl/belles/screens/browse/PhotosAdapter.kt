package dev.weihl.belles.screens.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import dev.weihl.belles.data.BellesImage
import dev.weihl.belles.databinding.ItemPhotosLayoutBinding

/**
 * @author Ngai
 */
class PhotosAdapter(
    @NonNull private val photoList: ArrayList<BellesImage>,
    @NonNull private val callBack: PhotosAdapterCallBack
) :
    RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val bind = ItemPhotosLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        bind.photosAdapterCallBack = callBack
        val viewHolder = PhotosViewHolder(bind.root)
        viewHolder.bind = bind
        viewHolder.bind.image.setOnOutsidePhotoTapListener { callBack.photoOutsideClick() }
        return viewHolder

    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    private fun photo(position: Int): BellesImage {
        return photoList[position]
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {

        val glideUrl = GlideUrl(
            photo(position).url,
            LazyHeaders.Builder().addHeader("Referer", photo(position).referer).build()
        )
        Glide.with(holder.bind.image.context)
            .load(glideUrl)
//            .placeholder(R.drawable.ic_action_load)
            .thumbnail(0.1f)
            .into(holder.bind.image)

    }

    // view holder
    class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bind: ItemPhotosLayoutBinding
    }

}

interface PhotosAdapterCallBack {

    fun photoOutsideClick()
}
