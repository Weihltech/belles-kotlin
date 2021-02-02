package dev.weihl.belles.screens.home.belles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.weihl.belles.R
import dev.weihl.belles.common.loadImage
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
        viewHolder.bind = bind
        return viewHolder
    }

    override fun onBindViewHolder(holder: BellesItemHolder, position: Int) {
        val itemBelles = getItem(position)
        //holder.bind.itemBelles = itemBelles
        //holder.bind.position = position

        runCatching {
            Timber.d("image load referer:${itemBelles.referer} ; cover:${itemBelles.thumb}")
            holder.context.loadImage(holder.bind.image, itemBelles.referer, itemBelles.thumb)
        }

        holder.bind.desc.text = itemBelles.title
        holder.bind.tagRecent.visibility = if (itemBelles.date == -1L) View.VISIBLE else View.GONE
        holder.bind.tagFavorite.setBackgroundResource(
            when (itemBelles.favorite) {
                0 -> R.drawable.ic_favorites
                else -> R.drawable.ic_favorites_mark
            }
        )

        holder.bind.tagFavorite.setTag(R.id.value, position)
        holder.bind.root.setTag(R.id.value, position)
        holder.bind.tagFavorite.setOnClickListener(onFavoriteClick)
        holder.bind.root.setOnClickListener(onItemClick)

        holder.bind.image.visibility = if (itemBelles.date == -2L) View.GONE else View.VISIBLE
    }

    class BellesItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bind: ItemBellesLayoutBinding
        val context: Context
            get() = bind.root.context
    }

    private val onItemClick = View.OnClickListener() {
        runCatching {
            val position = it.getTag(R.id.value) as Int
            val itemBelles = getItem(position)
            callBack.itemClick(itemBelles, position)
        }
    }

    private val onFavoriteClick = View.OnClickListener() {
        runCatching {
            val position = it.getTag(R.id.value) as Int
            Timber.d("onFavoriteClick:$position")
            val itemBelles = getItem(position)
            callBack.favoriteClick(itemBelles, position)
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

interface BellesAdapterCallBack {

    fun itemClick(itemBelles: Belles, position: Int)

    fun favoriteClick(itemBelles: Belles, position: Int)
}
