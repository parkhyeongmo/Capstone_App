package com.capstone.frontapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PartsRVAdapter() : RecyclerView.Adapter<PartsRVAdapter.ViewHolder>() {

    private val list = ArrayList<part>()

    fun setList(list: ArrayList<part>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.partslist_item, parent, false)
        return ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, part_id: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position, list[position].part_id)
            }
        }

        holder.bindItem(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {

            }
        }

        fun bindItem(item: part) {
            val partName = itemView.findViewById<TextView>(R.id.partName)
            val partStock = itemView.findViewById<TextView>(R.id.partStock)

            partName.text = item.part_name
            partStock.text = item.stock.toString()
        }

    }



}