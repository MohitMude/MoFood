package com.iitism.mofood.adapter

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.iitism.mofood.R
import com.iitism.mofood.database.OrderDatabase
import com.iitism.mofood.database.OrderEntity
import com.iitism.mofood.model.FoodItem

class DetailsAdapter(val context: Context, private val itemList:ArrayList<FoodItem>) :
    RecyclerView.Adapter<DetailsAdapter.DetailViewHolder>(){
    val Tag=R.color.colorPrimary
    class DetailViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtsno:TextView=view.findViewById(R.id.txt_view_sno)
        val texTitle: TextView =view.findViewById(R.id.txt_food_name)
        val texPrice: TextView =view.findViewById(R.id.txt_food_price)
        val btnAdd:Button=view.findViewById(R.id.btn_add)
        //val llayout: LinearLayout =view.findViewById(R.id.all_rest_layout)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.detail_card_view,parent,false)

        return DetailViewHolder(view )
    }

    override fun getItemCount(): Int {
       return  itemList.size
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val food= itemList[position]
        holder.texTitle.text=food.foodName
        holder.texPrice.text=food.foodCost
        holder.txtsno.text=(position+1).toString()
        var orderEntity=OrderEntity(
           food.foodId,food.foodName,food.foodCost
        )
        val checkAdded=DBAsynctask(context,orderEntity,1).execute()
        val isAdded=checkAdded.get()

        if(isAdded)
        {
            holder.btnAdd.setText(R.string.remove)
            holder.btnAdd.setTextColor(Color.WHITE)
            holder.btnAdd.setBackgroundColor(Color.YELLOW)
            holder.btnAdd.setTag(Tag,R.string.remove)
        }
        else
        {
            holder.btnAdd.setText(R.string.add)
            holder.btnAdd.setTextColor(Color.WHITE)
            holder.btnAdd.setBackgroundColor(Color.RED)
            holder.btnAdd.setTag(Tag,R.string.add)
        }

        holder.btnAdd.setOnClickListener{
            val tag=holder.btnAdd.getTag(Tag)
            if(tag==R.string.remove)
            {
                val async=DBAsynctask(context,orderEntity,3).execute()
                val result=async.get()

                if(result)
                {
                    holder.btnAdd.setText(R.string.add)
                    holder.btnAdd.setTextColor(Color.WHITE)
                    holder.btnAdd.setBackgroundColor(Color.RED)
                    holder.btnAdd.setTag(Tag,R.string.add)
                }
                else
                {
                    Toast.makeText(context,"Error occurred. Try Again!!",
                        Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                val async=DBAsynctask(context,orderEntity,2).execute()
                val result=async.get()

                if(result)
                {
                    holder.btnAdd.setText(R.string.remove)
                    holder.btnAdd.setTextColor(Color.WHITE)
                    holder.btnAdd.setBackgroundColor(Color.YELLOW)
                    holder.btnAdd.setTag(Tag,R.string.remove)
                }
                else
                {
                    Toast.makeText(context,"Error occurred. Try Again!!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    class DBAsynctask(val context: Context, private val orderEntity: OrderEntity, private val mode:Int) :
        AsyncTask<Void, Void, Boolean>()
    {
        private val db= Room.databaseBuilder(context,OrderDatabase::class.java,"Order").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode)
            {
                1 ->
                {
                    //restaurant is fav or not
                    val food: OrderEntity?=db.orderDao().getOrderById(orderEntity.foodItemId)
                    db.close()
                    return food!=null
                }
                2 ->
                {
                    //insert restaurant
                    db.orderDao().insert(orderEntity)
                    db.close()
                    return true
                }
                3 ->
                {
                    //remove
                    db.orderDao().delete(orderEntity)
                    db.close()
                    return true
                }
            }

            return false
        }
    }
}