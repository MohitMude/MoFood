package com.iitism.mofood

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class OrderPlacedActivity : AppCompatActivity() {
    private val timeout:Long=1500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)


        Handler().postDelayed({
                val intent= Intent(this@OrderPlacedActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
        },timeout)
    }
}
