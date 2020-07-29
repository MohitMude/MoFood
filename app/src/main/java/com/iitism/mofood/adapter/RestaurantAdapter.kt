package com.iitism.mofood.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.iitism.mofood.R
import com.iitism.mofood.RestaurantDetailActivity
import com.iitism.mofood.database.RestaurantDatabase
import com.iitism.mofood.database.RestaurantEntity
import com.iitism.mofood.model.Restaurants
import com.squareup.picasso.Picasso

class RestaurantAdapter(val context: Context, private var itemList:ArrayList<Restaurants>) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>()
{
    val Tag=R.color.colorPrimary



    class RestaurantViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val texTitle: TextView =view.findViewById(R.id.home_txt_name)
        val texPrice:TextView=view.findViewById(R.id.home_txt_price)
        val texRating:TextView=view.findViewById(R.id.home_txt_rating)
        val imgRest: ImageView =view.findViewById(R.id.home_image)
        val isfavimg: ImageView=view.findViewById(R.id.img_fav)
        val llayout: LinearLayout =view.findViewById(R.id.all_rest_layout)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_card_view,parent,false)

        return RestaurantViewHolder(view )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val rest= itemList[position]
        holder.texTitle.text=rest.restauarantName
        holder.texPrice.text=rest.restauarantCost
        holder.texRating.text=rest.restauarantRating
        Picasso.get().load(rest.restauarantImage).error(R.drawable.ic_launcher_foreground).into(holder.imgRest)

        var restEntity=RestaurantEntity(
            rest.restauarantId , rest.restauarantName, rest.restauarantRating , rest.restauarantCost , rest.restauarantImage
        )
        val checkfav=DBAsynctask(context,restEntity,1).execute()
        val isFav=checkfav.get()

        if(isFav)
        {
            holder.isfavimg.setImageResource(R.drawable.ic_favorite_filled)
            holder.isfavimg.setTag(Tag,R.drawable.ic_favorite_filled)
        }
        else
        {
            holder.isfavimg.setImageResource(R.drawable.ic_favorite_border)
            holder.isfavimg.setTag(Tag,R.drawable.ic_favorite_border)
        }

        holder.isfavimg.setOnClickListener{

            val tag=holder.isfavimg.getTag(Tag)
            if(tag==R.drawable.ic_favorite_filled)
            {
                val async=DBAsynctask(context,restEntity,3).execute()
                val result=async.get()

                if(result)
                {
                    Toast.makeText(context," Removed from favourite",
                        Toast.LENGTH_SHORT).show()
                    holder.isfavimg.setImageResource(R.drawable.ic_favorite_border)
                    holder.isfavimg.setTag(Tag,R.drawable.ic_favorite_border)
                }
                else
                {
                    Toast.makeText(context,"Error occurred. Try Again!!",
                        Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                val async=DBAsynctask(context,restEntity,2).execute()
                val result=async.get()

                if(result)
                {
                    Toast.makeText(context," Added to favourite", Toast.LENGTH_SHORT).show()
                    holder.isfavimg.setImageResource(R.drawable.ic_favorite_filled)
                    holder.isfavimg.setTag(Tag,R.drawable.ic_favorite_filled)
                }
                else
                {
                    Toast.makeText(context,"Error occurred. Try Again!!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.llayout.setOnClickListener {
            val intent=Intent(context,RestaurantDetailActivity::class.java)
            intent.putExtra("rest_id",rest.restauarantId)
            intent.putExtra("rest_name",rest.restauarantName)
            intent.putExtra("rest_rating",rest.restauarantRating)
            intent.putExtra("rest_image",rest.restauarantImage)
            intent.putExtra("rest_cost",rest.restauarantCost)
            context.startActivity(intent)
        }
    }


    class DBAsynctask(val context: Context, private val restEntity: RestaurantEntity, private val mode:Int) :
        AsyncTask<Void, Void, Boolean>()
    {
        private val db= Room.databaseBuilder(context, RestaurantDatabase::class.java,"Restaurants").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode)
            {
                1 ->
                {
                    //restaurant is fav or not
                    val book: RestaurantEntity?=db.restaurantDao().getRestaurantById(restEntity.restId)
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


    fun filterList(filterdNames: ArrayList<Restaurants>) {
        this.itemList = filterdNames
        notifyDataSetChanged()
    }

}