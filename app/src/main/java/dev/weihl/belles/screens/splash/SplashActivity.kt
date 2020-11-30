package dev.weihl.belles.screens.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import dev.weihl.belles.R
import dev.weihl.belles.screens.BasicActivity
import dev.weihl.belles.screens.home.HomeActivity

class SplashActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 1000)
    }
}