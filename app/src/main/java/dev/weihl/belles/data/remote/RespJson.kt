package dev.weihl.belles.data.remote

/**
 * @desc 相应
 *
 * @author Weihl Created by 2019/12/4
 *
 */

class RespJson {
    var status: Int = 0
    lateinit var message: String
    lateinit var list: ArrayList<String>
}