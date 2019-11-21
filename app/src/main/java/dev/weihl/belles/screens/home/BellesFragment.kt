package dev.weihl.belles.screens.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dev.weihl.belles.R
import dev.weihl.belles.databinding.FragmentBellesBinding
import kotlinx.android.synthetic.main.fragment_belles.*

/**
 * A simple [Fragment] subclass.
 */
class BellesFragment : Fragment() {

    private lateinit var binding: FragmentBellesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_belles, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.button2?.setOnClickListener {
            button2?.findNavController()
                ?.navigate(BellesFragmentDirections.actionBellesFragmentToCollectFragment("From BellesFragment"))

        }

    }


}
