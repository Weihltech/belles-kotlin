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
import dev.weihl.belles.data.SexyImage
import dev.weihl.belles.data.json2SexyImageList
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.ItemFavoriteLayoutBinding


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
        bind.bellesAdapterCallBack = callBack
        viewHolder.bind = bind
        viewHolder.context = parent.context
        return viewHolder
    }

    override fun onBindViewHolder(holder: BellesItemHolder, position: Int) {
        val itemBelles = getItem(position)
        holder.bind.itemBelles = itemBelles
        holder.bind.title.text = itemBelles.title
        holder.bind.tagFavorite.setBackgroundResource(
            if (itemBelles.favorite == "yes")
                R.drawable.ic_favorites_mark
            else R.drawable.ic_favorites
        )

        val imgList = json2SexyImageList(itemBelles.details)
        if (imgList != null && imgList.isNotEmpty()) {
            dispatchImg(holder, imgList)
        } else {
            glideLoad(holder.context, itemBelles.thumb, itemBelles.referer, holder.bind.imgLeft)
        }

    }

    private fun dispatchImg(holder: BellesItemHolder, imgList: ArrayList<SexyImage>) {
        loop@ for (index in 0..imgList.size) {
            when (index) {
                0 -> {
                    glideLoad(
                        holder.context, imgList[0].url, imgList[0].referer,
                        holder.bind.imgLeft
                    )
                }
                1 -> {
                    glideLoad(
                        holder.context, imgList[1].url, imgList[1].referer,
                        holder.bind.imgRight1
                    )
                }
                2 -> {
                    glideLoad(
                        holder.context, imgList[2].url, imgList[2].referer,
                        holder.bind.imgRight2
                    )
                }
                3 -> {
                    glideLoad(
                        holder.context, imgList[3].url, imgList[3].referer,
                        holder.bind.imgRight3
                    )
                }
                4 -> {
                    glideLoad(
                        holder.context, imgList[4].url, imgList[4].referer,
                        holder.bind.imgRight4
                    )
                }
                5 -> {
                    glideLoad(
                        holder.context, imgList[5].url, imgList[5].referer,
                        holder.bind.imgBottom
                    )
                }
                else -> break@loop
            }
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

    fun itemClick(itemBelles: Belles)

    fun favoriteClick(itemBelles: Belles)
}
