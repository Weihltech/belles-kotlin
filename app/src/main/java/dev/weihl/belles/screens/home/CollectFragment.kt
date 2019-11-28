package dev.weihl.belles.screens.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import dev.weihl.belles.R
import dev.weihl.belles.databinding.FragmentCollectBinding


class CollectFragment : Fragment() {

    private lateinit var binding: FragmentCollectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_collect, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            var bundle = CollectFragmentArgs.fromBundle(arguments!!)
            binding.message.text = bundle.collectArgs
        } catch (e: Exception) {
        }

    }

}
