package dev.weihl.belles.screens.home.belles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.weihl.belles.R
import dev.weihl.belles.data.local.belles.Belles
import dev.weihl.belles.databinding.LayoutItemBellesBinding

/**
 * @desc Belles 列表 Adapter
 *
 * @author Weihl Created by 2019/12/3
 *
 */
class BellesAdapter : RecyclerView.Adapter<BellesAdapter.BellesItemHolder>() {

    var data = listOf<Belles>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellesItemHolder {
        val itemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_item_belles,
            parent, false
        ) as LayoutItemBellesBinding
        val viewHolder = BellesItemHolder(itemBinding.root)
        viewHolder.bind = itemBinding
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BellesItemHolder, position: Int) {
        val itemBelles = data[position]
        holder.bind.title.text = itemBelles.title
        holder.bind.desc.text = itemBelles.desc
    }


    class BellesItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var bind: LayoutItemBellesBinding
    }
}

//class SleepNightDiffCallback :
//    DiffUtil.ItemCallback<Belles>() {
//    override fun areItemsTheSame(oldItem: Belles, newItem: Belles): Boolean {
//
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Belles, newItem: Belles): Boolean {
//
//        return oldItem == newItem
//    }
//}
