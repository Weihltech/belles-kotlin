package dev.weihl.belles.screens.home.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.weihl.belles.R
import dev.weihl.belles.databinding.FragmentUserBinding
import dev.weihl.belles.screens.BasicFragment


class UserFragment : BasicFragment() {

    private lateinit var binding: FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }


}
