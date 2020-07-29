package com.iitism.mofood.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.iitism.mofood.R
import com.iitism.mofood.adapter.ParentAdapter
import com.iitism.mofood.model.ParentModel
import org.json.JSONException


class OrderHistoryFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var resInfoList= arrayListOf<ParentModel>()
    lateinit var adapter:ParentAdapter
    var userId:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view=inflater.inflate(R.layout.fragment_order_history, container, false)
        recyclerView=view.findViewById(R.id.order_history_recycler_view)
        layoutManager= LinearLayoutManager(activity)
        val pref=activity?.getSharedPreferences("user",Context.MODE_PRIVATE)
        if (pref != null) {
            userId=pref.getString("user_id","") ?:""
        }
          setVolley()
        return view
    }
    fun setVolley()
    {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"

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
                    //progresslayout.visibility=View.GONE
                    if (success) {

                        val data = re.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val ResJsonObject = data.getJSONObject(i)
                            val ResObject = ParentModel(
                                ResJsonObject.getString("order_id"),
                                ResJsonObject.getString("restaurant_name"),
                                ResJsonObject.getString("total_cost"),
                                ResJsonObject.getString("order_placed_at"),
                                ResJsonObject.getJSONArray("food_items")
                            )
                            resInfoList.add(ResObject)
                            adapter = ParentAdapter(activity as Context, resInfoList)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = layoutManager

                        }

                    } else {
                        if(activity!=null)
                            Toast.makeText(activity as Context, "Error Occurred", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
                catch (e: JSONException)
                {
                    if(activity!=null)
                        Toast.makeText(activity as Context, "Unexpected Error Occurred", Toast.LENGTH_SHORT)
                            .show()
                }

            }, Response.ErrorListener {
                if(activity!=null)
                    Toast.makeText(activity as Context, "Server Error Occurred", Toast.LENGTH_SHORT)
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

}
