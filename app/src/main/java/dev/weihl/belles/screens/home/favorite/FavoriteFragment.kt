package dev.weihl.belles.screens.home.favorite


import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dev.weihl.belles.common.SpaceItemDecoration
import dev.weihl.belles.data.BImage
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.FragmentFavoriteBinding
import dev.weihl.belles.extension.dp2Px
import dev.weihl.belles.extension.startPhotosActivity
import dev.weihl.belles.screens.BasicFragment
import timber.log.Timber


class FavoriteFragment : BasicFragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater)

        // recycler view
        val adapter = FavoriteAdapter(object : FavoriteAdapterCallBack {
            override fun itemClick(view: View, position: Int, itemBelles: Belles, bImage: BImage) {
                val globalVisibleRect = Rect()
                view.getLocalVisibleRect(globalVisibleRect)
                val globalXY = IntArray(2)
                view.getLocationOnScreen(globalXY)
                Timber.d("itemClick :　$position")

                context?.startPhotosActivity(
                    itemBelles.details, position,
                    globalXY,
                    globalVisibleRect,
                    bImage.referer,
                    bImage.url
                )
            }

            override fun favoriteClick(itemBelles: Belles) {
                favoriteViewModel.markFavorites(itemBelles)
                val adapter = binding.favoriteRecyclerView.adapter
                adapter?.notifyDataSetChanged()
            }
        })
        binding.favoriteRecyclerView.adapter = adapter
        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.favoriteRecyclerView.addItemDecoration(
            SpaceItemDecoration(requireContext().dp2Px(1), 1)
        )

        favoriteViewModel.bellesList.observe(viewLifecycleOwner, Observer {
            binding.empty.visibility = if (it == null || it.isEmpty()) {
                View.VISIBLE
                return@Observer
            } else View.GONE

            adapter.submitList(it) {
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteViewModel.queryAllFavoriteBelles()
    }

}
