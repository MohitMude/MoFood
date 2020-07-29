package com.iitism.mofood

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.AppBarLayout
import com.iitism.mofood.adapter.OrderAdapter
import com.iitism.mofood.database.OrderDatabase
import com.iitism.mofood.database.OrderEntity
import com.iitism.mofood.model.FoodItem
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyCartActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var orderList= listOf<OrderEntity>()
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var btnPlaceOrder: Button
    private lateinit var txtRestname:TextView
    lateinit var name:String
    lateinit var adapter:OrderAdapter
    var costList= listOf<String>()
    var idList= listOf<String>()
    var cost:Int=0
    lateinit var userId:String
    lateinit var restId:String
    var jsonParam=JSONObject()
    var jsonArray=JSONArray()
    var jsonObject=JSONObject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
        var pref=getSharedPreferences("user",Context.MODE_PRIVATE)
        userId=pref.getString("user_id","") ?:""

        recyclerView=findViewById(R.id.cart_recycler_view)
        toolbar=findViewById(R.id.cart_toolbar)
        appBarLayout=findViewById(R.id.cart_app_bar_layout)
        btnPlaceOrder=findViewById(R.id.btn_place_order)
        txtRestname=findViewById(R.id.cart_rest_name)
        restId=intent.getStringExtra("rest_id") ?:""
        name=intent.getStringExtra("rest_name") ?:""
        txtRestname.text="Order from: $name"
        layoutManager= LinearLayoutManager(this@MyCartActivity)
        orderList=RetrieveOrder(this@MyCartActivity).execute().get()
        costList=RetrieveCost(this@MyCartActivity).execute().get()
        idList=RetrieveFav(this@MyCartActivity).execute().get()
        recyclerView.setOnClickListener(this)
        costList.forEach {

            cost += it.toInt()

        }
        btnPlaceOrder.text="Place Order(Total : Rs.$cost)"


        settoolbar()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        adapter= OrderAdapter(this@MyCartActivity,orderList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=layoutManager
        btnPlaceOrder.setOnClickListener(this)
    }

    private fun settoolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    class RetrieveOrder(val context: Context) : AsyncTask<Void, Void, List<OrderEntity>>()
    {
        private val db= Room.databaseBuilder(context,OrderDatabase::class.java,"Order").build()

        override fun doInBackground(vararg p0: Void?): List<OrderEntity> {

            return db.orderDao().getAllOrders()
        }


    }
    class RetrieveCost(val context: Context) : AsyncTask<Void, Void, List<String>>()
    {
        private val db= Room.databaseBuilder(context,OrderDatabase::class.java,"Order").build()

        override fun doInBackground(vararg p0: Void?): List<String> {

            return db.orderDao().getCost()
        }


    }

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.btn_place_order ->{
                println("good")
                jsoncreate()
                println("good")
                setVolley()

            }

        }

    }
    fun jsoncreate()
    {
        try {
            jsonParam.put("user_id", userId)
            jsonParam.put("restaurant_id", restId)
            jsonParam.put("total_cost", cost.toString())
        }catch(e:JSONException)
        {
            e.printStackTrace()
        }


        idList.forEach {
            try {
                jsonObject.put("food_item_id", it)
                jsonArray.put(jsonObject)
            }catch(e:JSONException)
            {
                e.printStackTrace()
            }
        }
        try {
            jsonParam.put("food", jsonArray)
        }catch(e:JSONException)
        {
            e.printStackTrace()
        }

    }

    class RetrieveFav(val context: Context) : AsyncTask<Void, Void, List<String>>()
    {
        private val db= Room.databaseBuilder(context,OrderDatabase::class.java,"Order").build()

        override fun doInBackground(vararg p0: Void?): List<String> {

            return db.orderDao().getId()
        }


    }

    fun setVolley()
    {
        val queue= Volley.newRequestQueue(this@MyCartActivity)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"



        val jsonRequest=object : JsonObjectRequest(Request.Method.POST,url,jsonParam, Response.Listener {

            val re=it.getJSONObject("data")
            val success = re.getBoolean("success")
            println("response $it")
            println("response $re")
            println("response $success")
            try {
                // progressBar.visibility=View.GONE
                if (success) {

                    Toast.makeText(this@MyCartActivity, "Order Placed", Toast.LENGTH_SHORT)
                        .show()
                    Delete(this@MyCartActivity).execute().get()
                    val intent=Intent(this@MyCartActivity,OrderPlacedActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                else {
                    //if(activity!=null)
                    Toast.makeText(this@MyCartActivity, "Error occurred .Try later", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            catch (e: JSONException)
            {
                //if(activity!=null)
                Toast.makeText(this@MyCartActivity, "Unexpected Error Occurred "+e.message, Toast.LENGTH_SHORT)
                    .show()
            }

        }, Response.ErrorListener {

            Toast.makeText(this@MyCartActivity, "Server Error Occurred", Toast.LENGTH_SHORT)
                .show()
        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "7ff8a62bff3cd6"
                return headers
            }
        }
        queue.add(jsonRequest)
    }

    class Delete(val context: Context) : AsyncTask<Void, Void, Unit>()
    {
        private val db= Room.databaseBuilder(context, OrderDatabase::class.java,"Order").build()

        override fun doInBackground(vararg p0: Void?): Unit {

            return db.orderDao().delete()
        }


    }
}
