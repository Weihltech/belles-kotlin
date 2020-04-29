package dev.weihl.belles.screens.home

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dev.weihl.belles.R
import dev.weihl.belles.screens.BasicActivity
import dev.weihl.belles.work.CrawlerWork
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author Weihl
 */
class HomeActivity : BasicActivity() {

    // var 可变变量；val 不可变变量；
    private var currFragmentId: Int = R.id.bellesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        CrawlerWork.enqueue()

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
