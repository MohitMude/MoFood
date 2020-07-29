package com.iitism.mofood

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    private val timeout:Long=1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
           val sharedpref=getSharedPreferences("user",Context.MODE_PRIVATE)
            val username=sharedpref.getString("name","")
            if(username.equals(""))
            {
                val intent=Intent(this@SplashActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                val intent=Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },timeout)
    }
}
