package pers.vaccae.memorymonitor

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间： 15:16
 * 功能模块说明：
 */
class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this

        Monitor.init(mContext!!)
        Monitor.writeFilters("vaccae")
    }


    companion object {
        @JvmField
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
    }
}