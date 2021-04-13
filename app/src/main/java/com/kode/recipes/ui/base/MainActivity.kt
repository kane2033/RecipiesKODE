package com.kode.recipes.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kode.recipes.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}