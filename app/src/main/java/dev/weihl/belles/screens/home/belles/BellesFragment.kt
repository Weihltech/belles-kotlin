package dev.weihl.belles.screens.home.belles


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import dev.weihl.belles.R
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.databinding.FragmentBellesBinding
import dev.weihl.belles.screens.home.HomeViewModelFactory
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 *
 * 带参数跳转
 * button2?.findNavController()?.navigate(BellesFragmentDirections.actionBellesFragmentToCollectFragment("From BellesFragment"))
 *
 *
 */
class BellesFragment : Fragment() {

    private lateinit var binding: FragmentBellesBinding
    private lateinit var bellesModel: BellesViewModel

    init {
        Timber.tag("BellesFragment")
        Timber.d("init !")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("onCreateView !")
        // data binding and view model
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_belles, container, false
        )

        val application = requireNotNull(this.activity).application
        val homeViewModelFactory = HomeViewModelFactory(application)
        bellesModel = ViewModelProviders.of(this, homeViewModelFactory)
            .get(BellesViewModel::class.java)
        binding.bellesViewModel = bellesModel
        binding.lifecycleOwner = this

        // recycler view
        val adapter = BellesAdapter(object : BellesAdapterCallBack {
            override fun itemClick(itemBelles: Belles) {
                Toast.makeText(application, itemBelles.href, Toast.LENGTH_LONG).show()
            }
        })
        binding.bellesRecyclerView.adapter = adapter
        binding.bellesRecyclerView.layoutManager = GridLayoutManager(application, 2)

        bellesModel.allBelles.observe(viewLifecycleOwner, Observer {
            Timber.d("allBelles.observe !")
            adapter.submitList(it)
            binding.bellesRecyclerView.scrollToPosition(0)
        })

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
