package dev.weihl.belles.screens.home.belles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.weihl.belles.R
import dev.weihl.belles.data.local.belles.Belles
import dev.weihl.belles.databinding.LayoutItemBellesBinding
import timber.log.Timber

/**
 * @desc Belles 列表 Adapter
 *
 * @author Weihl Created by 2019/12/3
 *
 */
class BellesAdapter(val callBack: BellesAdapterCallBack) :
    ListAdapter<Belles, BellesAdapter.BellesItemHolder>(BellesDiffCallback()) {

//    var data = listOf<Belles>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellesItemHolder {
//        val itemBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context),
//            R.layout.layout_item_belles,
//            parent, false
//        ) as LayoutItemBellesBinding
        val bind = LayoutItemBellesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val viewHolder = BellesItemHolder(bind.root)
        bind.bellesAdapterCallBack = callBack
        viewHolder.bind = bind
        return viewHolder
    }

//    override fun getItemCount(): Int {
//        return data.size
//    }

    override fun onBindViewHolder(holder: BellesItemHolder, position: Int) {
        val itemBelles = getItem(position)
        holder.bind.itemBelles = itemBelles
        holder.bind.title.text = itemBelles.title
        holder.bind.desc.text = itemBelles.desc
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
