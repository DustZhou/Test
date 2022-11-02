package com.mdrj.wlt

import android.app.Application
/**
 *
 * @ProjectName: APP
 * @Package: com.example.myapplication
 * @Description: java类作用描述
 * @Author: ZHT
 * @CreateDate: 2022/10/29
 */
class APP : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: APP? = null
            private set
    }
}