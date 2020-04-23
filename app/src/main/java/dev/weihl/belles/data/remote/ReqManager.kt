package dev.weihl.belles.data.remote

import com.google.gson.Gson
import dev.weihl.belles.data.local.entity.Belles
import okhttp3.OkHttpClient
import okhttp3.Request



/**
 * @desc 网络请求
 *
 * @author Weihl Created by 2019/12/4
 *
 */


class ReqManager {

    fun testHttp() {

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://api.map.baidu.com/telematics/v3/weather?location=嘉兴&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
            .build()
        val response = client.newCall(request).execute()
        val result = response.body?.string()

        println(result)
        testGson()
    }

    private fun testGson() {
        val result = "{\"title\":\"title\",\"desc\":\"APP被用户自己禁用，请在控制台解禁\",\"url\":\"url\"}"
        println("----------")
        val resp = Gson().fromJson(result,
            Belles::class.java) as Belles
        println(resp.href)

        println("----------")
        val respS = Gson().toJson(resp)
        println(respS)
    }

}
