package dev.weihl.belles.screens.home.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import dev.weihl.belles.R
import dev.weihl.belles.data.BImage
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.ItemFavoriteLayoutBinding
import dev.weihl.belles.json2SexyImageList


/**
 * @desc Belles 列表 Adapter
 *
 * @author Weihl Created by 2019/12/3
 *
 */
class FavoriteAdapter(private val callBack: FavoriteAdapterCallBack) :
    ListAdapter<Belles, FavoriteAdapter.BellesItemHolder>(BellesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellesItemHolder {
        val bind = ItemFavoriteLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val viewHolder = BellesItemHolder(bind.root)
        viewHolder.bind = bind
        viewHolder.context = parent.context
        return viewHolder
    }

    override fun onBindViewHolder(holder: BellesItemHolder, position: Int) {
        val itemBelles = getItem(position)
        holder.bind.title.text = itemBelles.title
        holder.bind.tagFavorite.setBackgroundResource(
            when (itemBelles.favorite) {
                0 -> R.drawable.ic_favorites
                else -> R.drawable.ic_favorites_mark
            }
        )

        val imgList = json2SexyImageList(itemBelles.details)
        if (imgList.isNotEmpty()) {
            dispatchImg(holder, imgList)
        } else {
            glideLoad(holder.context, itemBelles.thumb, itemBelles.referer, holder.bind.imgLeft)
        }

        holder.bind.tagFavorite.setTag(R.id.value, position)
        holder.bind.tagFavorite.setOnClickListener(onFavoriteClock)
        for (index in 0..6) {
            findImageView(holder, index)?.let {
                it.setTag(R.id.position, position)
                it.setOnClickListener(onItemClock)
            }
        }
    }

    private fun dispatchImg(holder: BellesItemHolder, imgList: List<BImage>) {
        for (index in 0..imgList.size) {
            val imgView = findImageView(holder, index) ?: break
            val imgItem = imgList[index]
            imgView.setTag(R.id.value, imgItem)
            glideLoad(holder.context, imgItem.url, imgItem.referer, imgView)
        }
    }

    private fun findImageView(holder: BellesItemHolder, index: Int): ImageView? {
        return when (index) {
            0 -> holder.bind.imgLeft
            1 -> holder.bind.imgRight1
            2 -> holder.bind.imgRight2
            3 -> holder.bind.imgRight3
            4 -> holder.bind.imgRight4
            5 -> holder.bind.imgBottom
            else -> null
        }
    }

    private fun glideLoad(context: Context, thumb: String, referer: String, imgLeft: ImageView) {
        Glide.with(context).load(glideUrl(thumb, referer)).into(imgLeft)
    }

    private fun glideUrl(imgUrl: String, referer: String): GlideUrl {
        return GlideUrl(imgUrl, LazyHeaders.Builder().addHeader("Referer", referer).build())
    }

    class BellesItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bind: ItemFavoriteLayoutBinding
        lateinit var context: Context
    }

    private val onItemClock = View.OnClickListener {
        runCatching {
            val position = it.getTag(R.id.position) as Int
            val imgItem = it.getTag(R.id.value) as BImage
            val itemBelles = getItem(position)
            callBack.itemClick(it, itemBelles, imgItem)
        }
    }

    private val onFavoriteClock = View.OnClickListener() {
        runCatching {
            val position = it.getTag(R.id.value) as Int
            val itemBelles = getItem(position)
            callBack.favoriteClick(itemBelles)
        }
    }

}

class BellesDiffCallback :
    DiffUtil.ItemCallback<Belles>() {
    override fun areItemsTheSame(oldItem: Belles, newItem: Belles): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Belles, newItem: Belles): Boolean {
        return oldItem == newItem
    }
}

interface FavoriteAdapterCallBack {

    fun itemClick(view: View, itemBelles: Belles, bImage: BImage)

    fun favoriteClick(itemBelles: Belles)
}
