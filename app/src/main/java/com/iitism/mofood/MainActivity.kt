package com.iitism.mofood

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.iitism.mofood.fragment.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerlayout:DrawerLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var headername:TextView
    private lateinit var headermob:TextView
    var prev :MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerlayout=findViewById(R.id.drawer_layout)
        frameLayout=findViewById(R.id.frame_layout)
        navigationView=findViewById(R.id.navigation_view)
        toolbar=findViewById(R.id.toolbar)
        coordinatorLayout=findViewById(R.id.coordinator_layout)
        appBarLayout=findViewById(R.id.app_bar_layout)
        settoolbar()

        toolbar.setNavigationOnClickListener {
            drawerlayout.openDrawer(GravityCompat.START)
        }


        val actionBarDrawerToggle=ActionBarDrawerToggle(
        this@MainActivity,drawerlayout,
        R.string.opendrawer,R.string.closedrawer
        )

        drawerlayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        opendrawer()

       var header=navigationView.getHeaderView(0)
        headername=header.findViewById(R.id.drawer_user_name)
        headermob=header.findViewById(R.id.drawer_user_mobile_no)

        var pref=getSharedPreferences("user",Context.MODE_PRIVATE)
        headername.text=pref.getString("name","")
        headermob.text=pref.getString("mobile","")

        navigationView.setNavigationItemSelectedListener {

            if(prev!=null)
            {
                prev?.isChecked=false;
            }

            it.isCheckable=true
            it.isChecked=true
            prev=it

            when(it.itemId)
            {
                R.id.homebtn->{
                    opendrawer()
                    drawerlayout.closeDrawers()
                }
                R.id.order->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,OrderHistoryFragment())
                        .commit()
                    supportActionBar?.title = "Order History"
                    drawerlayout.closeDrawers()
                }
                R.id.profile->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, ProfileFragment())
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerlayout.closeDrawers()
                }
                R.id.favourite->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FavouriteFragment())
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerlayout.closeDrawers()
                }
                R.id.faq->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FAQFragment())
                        .commit()
                    supportActionBar?.title="FAQs"
                    drawerlayout.closeDrawers()
                }
                R.id.logout ->{
                  val builder=AlertDialog.Builder(this)
                    builder.setMessage("Do you want to logout from application?")
                        .setPositiveButton("Yes", DialogInterface.OnClickListener{
                            _,_ ->
                            val pref=getSharedPreferences("user",Context.MODE_PRIVATE)
                            pref.edit().clear().apply()
                            finish()
                        })
                        .setNegativeButton("No", DialogInterface.OnClickListener{
                            dialog,_ ->
                              dialog.cancel()
                        })


                    val alert=builder.create()
                    alert.show()
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }
    private fun settoolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun opendrawer()
    {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout,HomeFragment()).commit()
        navigationView.setCheckedItem(R.id.home)
        supportActionBar?.title="Home"
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.frame_layout))
        {
            !is HomeFragment->opendrawer()
            else -> super.onBackPressed()
        }

    }


}
