package com.iitism.mofood.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.iitism.mofood.R
import com.iitism.mofood.adapter.FavouriteRestAdapter
import com.iitism.mofood.database.RestaurantDatabase
import com.iitism.mofood.database.RestaurantEntity


class FavouriteFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var bookInfoList= arrayListOf<RestaurantEntity>()
    lateinit var progressbar: ProgressBar
    lateinit var progresslayout: RelativeLayout
    var restList= listOf<RestaurantEntity>()
    lateinit var adapter:FavouriteRestAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView=view.findViewById(R.id.fav_recycler_view)
        progressbar=view.findViewById(R.id.fav_progress_bar)
        progressbar.visibility=View.VISIBLE
        progresslayout=view.findViewById(R.id.fav_relative_layout)
        progresslayout.visibility=View.VISIBLE

        layoutManager= GridLayoutManager(activity as Context,2)
        restList=RetrieveFav(activity as Context).execute().get()

        if(activity!=null)
        {
            progresslayout.visibility=View.GONE
            adapter= FavouriteRestAdapter(activity as Context,restList)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=layoutManager
        }

        return view
    }


    class RetrieveFav(val context: Context) : AsyncTask<Void, Void, List<RestaurantEntity>>()
    {
        private val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"Restaurants").build()

        override fun doInBackground(vararg p0: Void?): List<RestaurantEntity> {

            return db.restaurantDao().getAllRestaurant()
        }


    }
}
