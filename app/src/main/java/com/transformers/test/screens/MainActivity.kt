package com.transformers.test.screens

import android.os.Bundle
import com.transformers.test.R
import com.transformers.test.platform.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
