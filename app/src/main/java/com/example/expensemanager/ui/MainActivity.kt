package com.example.expensemanager.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.expensemanager.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.background)
        }
    }
//    override fun onBackPressed() {
//        val count = supportFragmentManager.backStackEntryCount
//        if (count == 1) {
//            super.onBackPressed()
//            //additional code
//        } else {
//            supportFragmentManager.popBackStack()
//        }
//    }
}