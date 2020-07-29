package com.iitism.mofood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iitism.mofood.R
import com.iitism.mofood.model.ChildModel
import com.iitism.mofood.model.ParentModel
import kotlinx.android.synthetic.main.order_history_card.view.*

class ParentAdapter (val context: Context, private val itemList:ArrayList<ParentModel>)
    : RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {
    private val viewPool=RecyclerView.RecycledViewPool()

    class ParentViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val txtRestName:TextView=view.findViewById(R.id.order_history_rest_name)
        val txtDate:TextView=view.findViewById(R.id.order_history_placed_date)
        val recyclerView:RecyclerView=view.findViewById(R.id.child_recycler_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.order_history_card,parent,false)

        return ParentViewHolder(view )
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val rest = itemList[position]
        holder.txtRestName.text = rest.restaurantName
        holder.txtDate.text = rest.orderPlacedAt
        val childLayoutManager =
            LinearLayoutManager(holder.recyclerView.context, RecyclerView.VERTICAL, false)
        val jsonArray = rest.childArray
        val childList = arrayListOf<ChildModel>()

        for (i in 0 until jsonArray.length()) {
            val resJsonObject = jsonArray.getJSONObject(i)
            val resObject = ChildModel(
                resJsonObject.getString("food_item_id"),
                resJsonObject.getString("name"),
                resJsonObject.getString("cost")
            )
            childList.add(resObject)

            holder.recyclerView.apply {
                layoutManager = childLayoutManager
                adapter = ChildAdapter(childList)
                setRecycledViewPool(viewPool)
            }
        }
    }

}