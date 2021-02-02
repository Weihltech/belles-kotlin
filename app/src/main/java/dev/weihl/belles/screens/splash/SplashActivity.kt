package dev.weihl.belles.screens.splash

import android.content.Intent
import android.os.Bundle
import dev.weihl.belles.databinding.ActivitySplashBinding
import dev.weihl.belles.screens.BasicActivity
import dev.weihl.belles.screens.home.HomeActivity

class SplashActivity : BasicActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.postDelayed(
            {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, 1000
        )
    }
}