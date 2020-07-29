package com.iitism.mofood.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.iitism.mofood.R
import com.iitism.mofood.adapter.RestaurantAdapter
import com.iitism.mofood.model.Restaurants
import org.json.JSONException


class HomeFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var resInfoList= arrayListOf<Restaurants>()
    lateinit var adapter:RestaurantAdapter
    lateinit var progresslayout: RelativeLayout
    lateinit var progressbar: ProgressBar
    lateinit var search:EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        retainInstance = true
        recyclerView=view.findViewById(R.id.home_recycler_view)
        progresslayout=view.findViewById(R.id.home_progress_layout)
        progressbar=view.findViewById(R.id.home_progress_bar)
        search=view.findViewById(R.id.search_view)
        progresslayout.visibility=View.VISIBLE

        layoutManager= LinearLayoutManager(activity)
        setVolley()
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }

            private fun filter(so: String) {
                val filterdNames: ArrayList<Restaurants> = ArrayList()

                //looping through existing elements
                for (se in resInfoList) {
                    //if the existing elements contains the search input
                    if (se.getName().toLowerCase().contains(so.toLowerCase())) {
                        //adding the element to filtered list
                        filterdNames.add(se)
                    }
                }

                //calling a method of the adapter class and passing the filtered list
                adapter.filterList(filterdNames)
            }
        })

        return view
    }
   fun setVolley()
   {
       val queue = Volley.newRequestQueue(activity as Context)
       val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

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
                       progresslayout.visibility=View.GONE
                       if (success) {

                           val data = re.getJSONArray("data")
                           for (i in 0 until data.length()) {
                               val ResJsonObject = data.getJSONObject(i)
                               val ResObject = Restaurants(
                                   ResJsonObject.getString("id"),
                                   ResJsonObject.getString("name"),
                                   ResJsonObject.getString("rating"),
                                   ResJsonObject.getString("cost_for_one"),
                                   ResJsonObject.getString("image_url")
                               )
                               resInfoList.add(ResObject)
                               adapter = RestaurantAdapter(activity as Context, resInfoList)
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
