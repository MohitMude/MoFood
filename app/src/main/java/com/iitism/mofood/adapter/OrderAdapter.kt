package com.iitism.mofood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iitism.mofood.R
import com.iitism.mofood.database.OrderEntity

class OrderAdapter(val context: Context, private val itemList:List<OrderEntity>) :
            RecyclerView.Adapter<OrderAdapter.OrderViewHolder>(){



    class OrderViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val textSNo:TextView=view.findViewById(R.id.order_s_no)
        val textFoodName:TextView=view.findViewById(R.id.order_food_name)
        val textCost:TextView=view.findViewById(R.id.order_food_cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.order_card,parent,false)

        return OrderViewHolder(view )
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val food= itemList[position]
       holder.textSNo.text=(position+1).toString()
       holder.textFoodName.text= food.foodName
       holder.textCost.text=food.foodCost
    }

}