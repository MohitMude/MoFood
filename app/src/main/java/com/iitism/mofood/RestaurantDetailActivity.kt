package com.iitism.mofood

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
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
import com.iitism.mofood.adapter.DetailsAdapter
import com.iitism.mofood.database.OrderDatabase
import com.iitism.mofood.database.RestaurantDatabase
import com.iitism.mofood.database.RestaurantEntity
import com.iitism.mofood.model.FoodItem
import org.json.JSONException

class RestaurantDetailActivity : AppCompatActivity() ,View.OnClickListener{
    val Tag=R.color.colorPrimary
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var resInfoList= arrayListOf<FoodItem>()
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var favImage: ImageView
    private lateinit var btnProceed: Button
    lateinit var adapter:DetailsAdapter
    lateinit var id:String
    lateinit var name:String
    lateinit var rating:String
    lateinit var cost:String
    lateinit var imageUrl:String
    lateinit var restEntity:RestaurantEntity

    override fun onBackPressed() {

        Delete(this@RestaurantDetailActivity).execute().get()
        finish()
        super.onBackPressed()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        id=intent.getStringExtra("rest_id") ?:""
        name=intent.getStringExtra("rest_name") ?:""
        rating=intent.getStringExtra("rest_rating") ?:""
        cost=intent.getStringExtra("rets_cost") ?:""
        imageUrl=intent.getStringExtra("rest_image" ) ?:""

        recyclerView=findViewById(R.id.detail_recycler_view)
        toolbar=findViewById(R.id.detail_toolbar)
        appBarLayout=findViewById(R.id.detail_app_bar_layout)
        favImage=findViewById(R.id.detail_is_fav_icon)
        btnProceed=findViewById(R.id.btn_proceed_to_cart)
        btnProceed.setOnClickListener(this)
        settoolbar()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
         restEntity=RestaurantEntity(
           id , name, rating , cost , imageUrl
        )
        val checkfav=DBAsynctask(applicationContext,restEntity,1).execute()
        val isFav=checkfav.get()

        if(isFav)
        {
            favImage.setImageResource(R.drawable.ic_favorite_filled)
            favImage.setTag(Tag,R.drawable.ic_favorite_filled)
        }
        else
        {
            favImage.setImageResource(R.drawable.ic_favorite_border)
            favImage.setTag(Tag,R.drawable.ic_favorite_border)
        }


        favImage.setOnClickListener(this)
        layoutManager= LinearLayoutManager(this@RestaurantDetailActivity)
        recyclerView.setOnClickListener(this)
        setVolley()
    }

    private fun settoolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setVolley()
    {
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$id"

        //  {
        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                println("response $it")
                val re=it.getJSONObject("data")
                println("response $re")
                //val success = it.getBoolean("success")
                val success = re.getBoolean("success")
                println("response $success")
                try {
                  //  progresslayout.visibility= View.GONE
                    if (success) {

                        val data = re.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val ResJsonObject = data.getJSONObject(i)
                            val ResObject = FoodItem(
                                ResJsonObject.getString("id"),
                                ResJsonObject.getString("name"),
                                ResJsonObject.getString("cost_for_one"),
                                ResJsonObject.getString("restaurant_id")

                            )
                            resInfoList.add(ResObject)
                            adapter = DetailsAdapter(applicationContext, resInfoList)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = layoutManager

                        }

                    } else {
                        //if(activity!=null)
                            Toast.makeText(this@RestaurantDetailActivity, "Error Occurred", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
                catch (e: JSONException)
                {
                   // if(activity!=null)
                        Toast.makeText(this@RestaurantDetailActivity, "Unexpected Error Occurred", Toast.LENGTH_SHORT)
                            .show()
                }

            }, Response.ErrorListener {
               // if(activity!=null)
                    Toast.makeText(this@RestaurantDetailActivity, "Server Error Occurred", Toast.LENGTH_SHORT)
                        .show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "7ff8a62bff3cd6"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
        // }



    }

    class DBAsynctask(val context: Context, private val restEntity: RestaurantEntity, private val mode:Int) :
        AsyncTask<Void, Void, Boolean>()
    {
        private val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"Restaurants").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode)
            {
                1 ->
                {
                    //restaurant is fav or not
                    val book:RestaurantEntity?=db.restaurantDao().getRestaurantById(restEntity.restId)
                    db.close()
                    return book!=null
                }
                2 ->
                {
                    //insert restaurant
                    db.restaurantDao().insert(restEntity)
                    db.close()
                    return true
                }
                3 ->
                {
                    //remove
                    db.restaurantDao().delete(restEntity)
                    db.close()
                    return true
                }
            }

            return false
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.detail_is_fav_icon ->{

                 val tag=favImage.getTag(Tag)
                if(tag==R.drawable.ic_favorite_filled)
                {
                    try {
                        val async = DBAsynctask(applicationContext, restEntity, 3).execute()
                        val result = async.get()

                        if (result) {
                            Toast.makeText(
                                this@RestaurantDetailActivity,
                                " Removed from favourite",
                                Toast.LENGTH_SHORT
                            ).show()
                            favImage.setImageResource(R.drawable.ic_favorite_border)
                            favImage.setTag(Tag,R.drawable.ic_favorite_border)
                        } else {
                            Toast.makeText(
                                this@RestaurantDetailActivity,
                                "Error occurred. Try Again!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }catch (e: Exception)
                    {
                        Toast.makeText(
                            this@RestaurantDetailActivity,
                            "Error $e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else
                {
                    val async=DBAsynctask(applicationContext,restEntity,2).execute()
                    val result=async.get()

                    if(result)
                    {
                        Toast.makeText(this@RestaurantDetailActivity," Added to favourite",Toast.LENGTH_SHORT).show()
                        favImage.setImageResource(R.drawable.ic_favorite_filled)
                        favImage.setTag(Tag,R.drawable.ic_favorite_filled)
                    }
                    else
                    {
                        Toast.makeText(this@RestaurantDetailActivity,"Error occurred. Try Again!!",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.btn_proceed_to_cart ->{
                if(resInfoList.isNotEmpty()){
                val intent=Intent(this@RestaurantDetailActivity,MyCartActivity::class.java)
                intent.putExtra("rest_id",restEntity.restId)
                intent.putExtra("rest_name",restEntity.restName)

                startActivity(intent)
                finish()}
                else
                {
                    Toast.makeText(this@RestaurantDetailActivity,"Please select food item",Toast.LENGTH_SHORT).show()
                }

            }
            R.id.detail_recycler_view ->{
                adapter.notifyDataSetChanged()
                if(resInfoList.isNotEmpty())
                {
                    btnProceed.visibility=View.VISIBLE
                }
                else
                {
                    btnProceed.visibility=View.GONE
                }
            }
        }
    }

    class Delete(val context: Context) : AsyncTask<Void, Void, Unit>()
    {
        private val db= Room.databaseBuilder(context, OrderDatabase::class.java,"Order").build()

        override fun doInBackground(vararg p0: Void?): Unit {

            return db.orderDao().delete()
        }


    }

}
