package com.iitism.mofood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iitism.mofood.R
import com.iitism.mofood.model.ChildModel

class ChildAdapter(private val children: ArrayList<ChildModel>)
    : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {


    class ChildViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtFoodName:TextView=view.findViewById(R.id.order_history_child_food_name)
        val txtFoodPrice:TextView=view.findViewById(R.id.order_history_child_placed_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.child_card,parent,false)

        return ChildViewHolder(view )
    }

    override fun getItemCount(): Int {
       return children.size
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val child=children[position]

        holder.txtFoodName.text=child.foodName
        holder.txtFoodPrice.text=child.foodCost
    }
}