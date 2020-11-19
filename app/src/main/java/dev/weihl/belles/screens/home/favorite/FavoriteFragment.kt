package dev.weihl.belles.screens.home.favorite


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dev.weihl.belles.R
import dev.weihl.belles.common.SpaceItemDecoration
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.FragmentFavoriteBinding
import dev.weihl.belles.dp2Px
import dev.weihl.belles.screens.BasicFragment
import dev.weihl.belles.screens.browse.PhotosActivity
import timber.log.Timber


class FavoriteFragment : BasicFragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_favorite, container, false)

        val application = requireNotNull(this.activity).application
        binding.lifecycleOwner = this

        // recycler view
        val adapter = FavoriteAdapter(object : FavoriteAdapterCallBack {
            override fun itemClick(itemBelles: Belles) {
                val photoIntent = Intent(context, PhotosActivity::class.java)
                photoIntent.putExtra("details", itemBelles.details)
                startActivity(photoIntent)
            }

            override fun favoriteClick(itemBelles: Belles) {
                favoriteViewModel.markFavorites(itemBelles)
                val adapter = binding.favoriteRecyclerView.adapter
                adapter?.notifyDataSetChanged()
            }
        })
        binding.favoriteRecyclerView.adapter = adapter
        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(application, 1)
        binding.favoriteRecyclerView.addItemDecoration(
            SpaceItemDecoration(dp2Px(application, 1), 1)
        )

        favoriteViewModel.subBelles.observe(viewLifecycleOwner, Observer {
            Timber.d("refresh new Belles list ! ${it.size}")
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
