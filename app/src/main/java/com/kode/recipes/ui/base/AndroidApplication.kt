package com.kode.recipes.ui.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Входная точка для Hilt di.
 * */
@HiltAndroidApp
class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}