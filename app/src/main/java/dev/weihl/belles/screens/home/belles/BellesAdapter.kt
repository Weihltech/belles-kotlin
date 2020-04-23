package dev.weihl.belles.screens.home.belles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.LayoutItemBellesBinding

/**
 * @desc Belles 列表 Adapter
 *
 * @author Weihl Created by 2019/12/3
 *
 */
class BellesAdapter(private val callBack: BellesAdapterCallBack) :
    ListAdapter<Belles, BellesAdapter.BellesItemHolder>(BellesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellesItemHolder {
        val bind = LayoutItemBellesBinding.inflate(
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
        Glide.with(holder.bind.image.context)
            .load(itemBelles.thumb)
            .into(holder.bind.image)
        holder.bind.desc.text = itemBelles.href
    }


    class BellesItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bind: LayoutItemBellesBinding
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

    fun itemClick(itemBelles: Belles)
}
