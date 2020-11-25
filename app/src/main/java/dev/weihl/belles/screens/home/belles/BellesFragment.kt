package dev.weihl.belles.screens.home.belles


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dev.weihl.belles.R
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
class BellesFragment : BasicFragment() {

    private lateinit var binding: FragmentBellesBinding

    private val bellesViewModel: BellesViewModel by activityViewModels()

    init {
        Timber.tag("BellesFragment")
        Timber.d("init !")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.d("onCreateView !")
        // data binding and view model
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_belles, container, false
        )

        val application = requireNotNull(this.activity).application
        binding.lifecycleOwner = this

        // recycler view
        val adapter = BellesAdapter(object : BellesAdapterCallBack {
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
                startActivity(photoIntent)
            }

            override fun favoriteClick(itemBelles: Belles) {
                bellesViewModel.markFavorites(itemBelles)
                val adapter = binding.bellesRecyclerView.adapter
                adapter?.notifyDataSetChanged()
            }
        })
        binding.bellesRecyclerView.adapter = adapter
        binding.bellesRecyclerView.layoutManager = GridLayoutManager(application, 2)
        binding.bellesRecyclerView.addItemDecoration(SpaceItemDecoration(dp2Px(application, 4), 2))

        binding.swipeRefreshLayout.setOnRefreshListener {
            Timber.d("loadNextBelles !")
            bellesViewModel.loadNextBelles()
        }

        bellesViewModel.subBelles.observe(viewLifecycleOwner, Observer {
            Timber.d("refresh new Belles list !")
            adapter.submitList(it) {
                adapter.notifyDataSetChanged()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        })

        // default
        bellesViewModel.defaultNextBelles()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated !")


    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy !")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach !")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach !")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onAttach !")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause !")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate !")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart !")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop !")
    }

}
