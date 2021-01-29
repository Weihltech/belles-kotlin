package dev.weihl.belles.screens.home.belles


import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.weihl.belles.R
import dev.weihl.belles.common.SpaceItemDecoration
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.req.EnumAlbum
import dev.weihl.belles.databinding.FragmentBellesBinding
import dev.weihl.belles.dp2Px
import dev.weihl.belles.drawableResources
import dev.weihl.belles.screens.BasicFragment
import dev.weihl.belles.screens.browse.PhotosActivity
import timber.log.Timber
import kotlin.math.abs

/**
 * A simple [Fragment] subclass.
 *
 * 带参数跳转
 * button2?.findNavController()?.navigate(BellesFragmentDirections.actionBellesFragmentToCollectFragment("From BellesFragment"))
 *
 *
 */
class BellesFragment : BasicFragment(), BellesAdapterCallBack {

    private lateinit var binding: FragmentBellesBinding
    private val bellesViewModel: BellesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // data binding and view model
        binding = FragmentBellesBinding.inflate(inflater)

        // recycler view
        val context = requireContext()
        val adapter = BellesAdapter(this)
        binding.bellesRecyclerView.adapter = adapter
        binding.bellesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.bellesRecyclerView.addItemDecoration(
            SpaceItemDecoration(context.dp2Px(4), 2)
        )
        binding.bellesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // (dy > 0) //下滑动作
                // (dy < 0) //上滑动作
                if (abs(dy) > 10) {
                    binding.albumLayout.visibility = if (dy < 0) View.GONE else View.VISIBLE
                }
            }
        })

        // refresh action
        binding.swipeRefreshLayout.setOnRefreshListener {
            bellesViewModel.loadNextBelles()
        }

        // observe list data
        bellesViewModel.bellesList.observe(viewLifecycleOwner, Observer {
            Timber.d("refresh new Belles list !")
            adapter.submitList(it) {
                adapter.notifyDataSetChanged()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        })

        // Album tab layout
        val childLayout = binding.albumLayout.getChildAt(0)
        createAlbumTabs(childLayout as ViewGroup)
        childLayout.getChildAt(1).callOnClick()
        return binding.root
    }

    private fun createAlbumTabs(albumLayout: ViewGroup?) {
        albumLayout?.let {
            EnumAlbum.values().forEach { album ->
                val context = requireContext()
                val llp = LinearLayout.LayoutParams(
                    context.dp2Px(56),
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                llp.leftMargin = context.dp2Px(6)
                llp.rightMargin = context.dp2Px(6)
                llp.topMargin = context.dp2Px(4)
                llp.bottomMargin = context.dp2Px(4)
                val albumView = TextView(context).apply {
                    text = album.title
                    gravity = Gravity.CENTER
                    background = context.drawableResources(R.drawable.ic_round_bg)
                    setTextColor(Color.WHITE)
                }
                albumView.setTag(R.id.value, album)
                albumView.setOnClickListener(switchAlbumListener)
                it.addView(albumView, llp)
            }
        }
    }

    private val switchAlbumListener = View.OnClickListener {
        runCatching {
            if (binding.swipeRefreshLayout.isRefreshing) {
                Snackbar.make(it, "正在刷新中，请稍后...", 500).show()
                return@runCatching
            }
            val album = it.getTag(R.id.value) as EnumAlbum
            val hasData = bellesViewModel.switchAlbumTab(album)
            binding.swipeRefreshLayout.isRefreshing = true
            if (!hasData) {
                bellesViewModel.loadNextBelles()
            }
        }
    }

    override fun itemClick(itemBelles: Belles, position: Int) {
        val globalVisibleRect = Rect()
        val clickView = binding.bellesRecyclerView
            .layoutManager?.findViewByPosition(position)
        clickView?.getLocalVisibleRect(globalVisibleRect)
        val globalXY = IntArray(2)
        clickView?.getLocationOnScreen(globalXY)
        Timber.d("globalVisibleRect :　$globalVisibleRect ; globalXY ${globalXY.contentToString()}")

        val photoIntent = Intent(context, PhotosActivity::class.java)
        photoIntent.putExtra("details", itemBelles.details)
        photoIntent.putExtra("globalXY", globalXY)
        photoIntent.putExtra("globalRect", globalVisibleRect)
        startActivity(photoIntent)
    }

    override fun favoriteClick(itemBelles: Belles, position: Int) {
        bellesViewModel.markFavorites(itemBelles)
        Timber.d("123@@${binding.bellesRecyclerView.adapter}")
        binding.bellesRecyclerView.adapter?.notifyItemChanged(position)
    }

}
