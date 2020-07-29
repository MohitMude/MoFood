package com.iitism.mofood.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iitism.mofood.R
import com.iitism.mofood.RestaurantDetailActivity
import com.iitism.mofood.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavouriteRestAdapter (val context: Context, private val itemList: List<RestaurantEntity>):
   RecyclerView.Adapter <FavouriteRestAdapter.FavRestViewHolder>(){



   class FavRestViewHolder(view: View): RecyclerView.ViewHolder(view)
   {
      val texRestName: TextView =view.findViewById(R.id.txt_fav_res_name)
      val texRestPrice: TextView =view.findViewById(R.id.txt_fav_res_cost)
      val texRestRating: TextView =view.findViewById(R.id.txt_fav_res_rating)
      val imgRest: ImageView =view.findViewById(R.id.img_fav_res)
      val llayout: LinearLayout =view.findViewById(R.id.llFavContent)


   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavRestViewHolder {
      val view= LayoutInflater.from(parent.context).inflate(R.layout.fav_card_view,parent,false)

      return FavRestViewHolder(view )
   }

   override fun getItemCount(): Int {
     return itemList.size
   }

   override fun onBindViewHolder(holder: FavRestViewHolder, position: Int) {
      val rest = itemList[position]
      holder.texRestName.text = rest.restName
      holder.texRestPrice.text = rest.cost
      holder.texRestRating.text = rest.restRating
      Picasso.get().load(rest.imgUrl).error(R.drawable.ic_launcher_foreground).into(holder.imgRest)

      holder.llayout.setOnClickListener {
         val intent = Intent(context, RestaurantDetailActivity::class.java)
         intent.putExtra("rest_id",rest.restId)
         intent.putExtra("rest_name",rest.restName)
         intent.putExtra("rest_rating",rest.restRating)
         intent.putExtra("rest_image",rest.imgUrl)
         intent.putExtra("rest_cost",rest.cost)
         context.startActivity(intent)
      }
   }
}