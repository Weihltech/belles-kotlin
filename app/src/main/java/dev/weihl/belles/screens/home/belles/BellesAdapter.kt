package dev.weihl.belles.screens.home.belles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import dev.weihl.belles.R
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.ItemBellesLayoutBinding
import timber.log.Timber


/**
 * @desc Belles 列表 Adapter
 *
 * @author Weihl Created by 2019/12/3
 *
 */
class BellesAdapter(private val callBack: BellesAdapterCallBack) :
    ListAdapter<Belles, BellesAdapter.BellesItemHolder>(BellesDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellesItemHolder {
        val bind = ItemBellesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val viewHolder = BellesItemHolder(bind.root)
        bind.bellesAdapterCallBack = callBack
        viewHolder.bind = bind
        return viewHolder
    }

    override fun onBindViewHolder(holder: BellesItemHolder, position: Int) {
        val itemBelles = getItem(position)
        holder.bind.itemBelles = itemBelles
        holder.bind.position = position

        runCatching {
            Timber.d("image load referer:${itemBelles.referer} ; cover:${itemBelles.thumb}")
            val referer = LazyHeaders.Builder().addHeader("Referer", itemBelles.referer).build()
            val glideUrl = GlideUrl(itemBelles.thumb, referer)
            Glide.with(holder.bind.image.context).load(glideUrl).into(holder.bind.image)
        }

        holder.bind.desc.text = itemBelles.title
        holder.bind.tagRecent.visibility = if (itemBelles.date == -1L) View.VISIBLE else View.GONE
        holder.bind.tagFavorite.setBackgroundResource(
            if (itemBelles.favorite == "yes")
                R.drawable.ic_favorites_mark
            else R.drawable.ic_favorites
        )
    }


    class BellesItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bind: ItemBellesLayoutBinding
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

interface BellesAdapterCallBack {

    fun itemClick(itemBelles: Belles, position: Int)

    fun favoriteClick(itemBelles: Belles)
}
