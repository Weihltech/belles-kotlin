package dev.weihl.belles.screens.home.belles


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dev.weihl.belles.common.SpaceItemDecoration
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.FragmentBellesBinding
import dev.weihl.belles.dp2Px
import dev.weihl.belles.screens.BasicFragment
import dev.weihl.belles.screens.browse.PhotosActivity
import timber.log.Timber

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

        Timber.d("onCreateView !")
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            Timber.d("loadNextBelles !")
            bellesViewModel.loadNextBelles()
        }

        bellesViewModel.bellesList.observe(viewLifecycleOwner, Observer {
            Timber.d("refresh new Belles list !")
            adapter.submitList(it) {
                adapter.notifyDataSetChanged()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        })

        // load first data
        if (adapter.itemCount <= 0) {
            binding.swipeRefreshLayout.isRefreshing = true
            bellesViewModel.loadNextBelles()
        }
        return binding.root
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

    override fun favoriteClick(itemBelles: Belles) {
        bellesViewModel.markFavorites(itemBelles)
        val adapter = binding.bellesRecyclerView.adapter
        adapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated !")
    }

}
