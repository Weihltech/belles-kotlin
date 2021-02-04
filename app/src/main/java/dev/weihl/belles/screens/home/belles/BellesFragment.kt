package dev.weihl.belles.screens.home.belles


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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.weihl.belles.R
import dev.weihl.belles.common.SpaceItemDecoration
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.EnumAlbum
import dev.weihl.belles.databinding.FragmentBellesBinding
import dev.weihl.belles.extension.dp2Px
import dev.weihl.belles.extension.drawableResources
import dev.weihl.belles.extension.startPhotosActivity
import dev.weihl.belles.screens.BasicFragment
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
        binding.bellesRecyclerView.adapter = BellesAdapter(this)
        binding.bellesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.bellesRecyclerView.addItemDecoration(
            SpaceItemDecoration(context.dp2Px(4), 2)
        )
        binding.bellesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // (dy > 0) //下滑动作
                // (dy < 0) //上滑动作
                if (abs(dy) > 10) {
                    binding.albumBar.visibility = if (dy < 0) View.GONE else View.VISIBLE
                }
            }
        })

        // refresh action
        binding.swipeRefreshLayout.setOnRefreshListener {
            bellesViewModel.loadNextBelles()
            binding.albumBar.visibility = View.GONE
        }

        // observe list data
        bellesViewModel.bellesList.observe(viewLifecycleOwner, {
            Timber.d("refresh new Belles list !")
            getBellesAdapter()?.let { adapter ->
                adapter.submitList(it) {
                    adapter.notifyDataSetChanged()
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false
        })

        // album tab page
        bellesViewModel.albumPage.observe(viewLifecycleOwner, {
            // find album tab view
            // change text

            bellesViewModel.anEnum
        })

        // Album tab layout
        val childLayout = binding.albumLayout
        createAlbumTabs(childLayout as ViewGroup)
        childLayout.getChildAt(0).callOnClick()
        return binding.root
    }

    private fun createAlbumTabs(albumLayout: ViewGroup?) {
        albumLayout?.let {
            EnumAlbum.values().forEach { album ->
                val context = requireContext()
                val llp = LinearLayout.LayoutParams(
                    context.dp2Px(60),
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                llp.leftMargin = context.dp2Px(6)
                llp.rightMargin = context.dp2Px(6)
                llp.topMargin = context.dp2Px(6)
                llp.bottomMargin = context.dp2Px(12)
                llp.gravity = Gravity.TOP
                val albumView = TextView(context).apply {
                    text = album.title
                    gravity = Gravity.CENTER
                    background = context.drawableResources(R.drawable.ic_round_blue_bg)
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
            toggleAlbumTab(it)
            binding.albumBar.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = true
            if (!hasData) {
                bellesViewModel.loadNextBelles()
            }
        }
    }

    private fun toggleAlbumTab(view: View?) {
        view?.let {
            runCatching {
                for (index in 0 until binding.albumLayout.childCount) {
                    val tabView = binding.albumLayout.getChildAt(index)
                    tabView.background = it.context.drawableResources(R.drawable.ic_round_blue_bg)
                }
                it.background = it.context.drawableResources(R.drawable.ic_round_red_bg)
            }
        }
    }

    override fun itemClick(itemBelles: Belles, position: Int) {
        val globalVisibleRect = Rect()
        val clickView = binding.bellesRecyclerView.layoutManager?.findViewByPosition(position)
        clickView?.getLocalVisibleRect(globalVisibleRect)
        val globalXY = IntArray(2)
        clickView?.getLocationOnScreen(globalXY)
        Timber.d("globalVisibleRect :　$globalVisibleRect ; globalXY ${globalXY.contentToString()}")

        context?.startPhotosActivity(
            itemBelles.details, 0,
            globalXY,
            globalVisibleRect,
            itemBelles.referer,
            itemBelles.thumb
        )
    }

    override fun favoriteClick(itemBelles: Belles, position: Int) {
        bellesViewModel.markFavorites(itemBelles)
        Timber.d("123@@${binding.bellesRecyclerView.adapter}")
        binding.bellesRecyclerView.adapter?.notifyItemChanged(position)
    }

    private fun getBellesAdapter(): BellesAdapter? {
        if (binding.bellesRecyclerView.adapter == null) {
            return null
        }
        return binding.bellesRecyclerView.adapter as BellesAdapter
    }

}
