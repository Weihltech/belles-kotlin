package dev.weihl.belles.work

import dev.weihl.belles.work.mm131net.crawlerMM131WebTargetPage
import org.junit.Test

/**
 * @author Weihl Created by 2019/12/5
 */
class AppCrawlerWorkTest {

    @Test
    fun doWork() {

        crawlerMM131WebTargetPage("https://www.mm131.net/xinggan/5262.html")
    }
}