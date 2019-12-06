package dev.weihl.belles.screens.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dev.weihl.belles.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    private var currFragmentId: Int = R.id.bellesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation_view.setOnNavigationItemSelectedListener {

            if (currFragmentId != it.itemId) {
                NavigationUI.onNavDestinationSelected(
                    it, nav_host!!.findNavController()
                )
            }
            currFragmentId = it.itemId
            true
        }

    }


}
