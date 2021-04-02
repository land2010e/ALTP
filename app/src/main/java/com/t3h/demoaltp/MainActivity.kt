package com.t3h.demoaltp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var manager:MrgDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = MrgDatabase(this)

        findViewById<View>(R.id.btn_query).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        manager.get15Question()
    }
}