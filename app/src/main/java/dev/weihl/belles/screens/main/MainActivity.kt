package dev.weihl.belles.screens.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dev.weihl.belles.R
import dev.weihl.belles.databinding.ActivityMainBinding
import dev.weihl.belles.screens.BasicActivity

class MainActivity : BasicActivity() {


    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.button
        setContentView(R.layout.activity_main)
    }
}
