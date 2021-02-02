package dev.weihl.belles.screens.browse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.weihl.belles.common.loadImage
import dev.weihl.belles.data.BImage
import dev.weihl.belles.databinding.ItemPhotosLayoutBinding

/**
 * @author Ngai
 */
class PhotosAdapter(private val photoList: List<BImage>) :
    RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val bind = ItemPhotosLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val viewHolder = PhotosViewHolder(bind.root)
        viewHolder.bind = bind
        return viewHolder
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    private fun photo(position: Int): BImage {
        return photoList[position]
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = photo(position)
        holder.context.loadImage(holder.bind.image, photo.referer, photo.url)
    }

    // view holder
    class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bind: ItemPhotosLayoutBinding
        val context: Context
            get() = bind.root.context
    }

}
