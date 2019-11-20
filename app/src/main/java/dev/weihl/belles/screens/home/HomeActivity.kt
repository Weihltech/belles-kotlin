package dev.weihl.belles.screens.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dev.weihl.belles.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigationView.setOnNavigationItemSelectedListener {
            NavigationUI.onNavDestinationSelected(
                it, navHostFragment!!.findNavController()
            )
        }


    }
}
