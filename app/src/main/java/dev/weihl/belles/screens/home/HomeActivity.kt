package dev.weihl.belles.screens.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dev.weihl.belles.R
import dev.weihl.belles.databinding.ActivityHomeBinding
import dev.weihl.belles.screens.BasicActivity

/**
 * @author Weihl
 */
class HomeActivity : BasicActivity() {

    private val viewModel: HomeViewModel by viewModels()

    // var 可变变量；val 不可变变量；
    private var currFragmentId: Int = R.id.bellesFragment
    private lateinit var homeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        val navhost = supportFragmentManager.findFragmentByTag("nav_host_fragment")
                as NavHostFragment
        homeBinding.navigationView.setOnNavigationItemSelectedListener {

            if (currFragmentId != it.itemId) {
                homeBinding.toolBar.title = it.title
                NavigationUI.onNavDestinationSelected(
                    it, navhost.findNavController()
                )
            }
            currFragmentId = it.itemId
            true
        }
    }

}
