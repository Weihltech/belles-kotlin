package dev.weihl.belles.screens.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dev.weihl.belles.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    private var currFragmentId: Int = R.id.bellesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigationView.setOnNavigationItemSelectedListener {

            if (currFragmentId != it.itemId) {
                NavigationUI.onNavDestinationSelected(
                    it, navHost!!.findNavController()
                )
            }
            currFragmentId = it.itemId
            true
        }

    }


}
