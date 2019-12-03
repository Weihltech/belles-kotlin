package dev.weihl.belles.screens.home.belles


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dev.weihl.belles.R
import dev.weihl.belles.data.local.belles.BellesDB
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
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_belles, container, false)


        val application = requireNotNull(this.activity).application
        val dbDao = BellesDB.getInstance(application).bellesDBDao

        val homeViewModelFactory = HomeViewModelFactory(dbDao,application)
        bellesModel = ViewModelProviders.of(this, homeViewModelFactory)
            .get(BellesViewModel::class.java)
        binding.bellesViewModel = bellesModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated !")

//        bellesModel.count.observe(this, Observer {
//            binding.text.text = DateUtils.formatElapsedTime(it.toLong())
//        })

//        binding.startTimer.setOnClickListener {
//            bellesModel.onClick()
//        }

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
