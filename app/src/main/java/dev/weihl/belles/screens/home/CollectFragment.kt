package dev.weihl.belles.screens.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import dev.weihl.belles.R
import kotlinx.android.synthetic.main.fragment_collect.*


class CollectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            var bundle = CollectFragmentArgs.fromBundle(arguments!!)
            text.text = bundle.collectArgs
        } catch (e: Exception) {
        }

    }

}
