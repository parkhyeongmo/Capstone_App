package com.capstone.frontapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InspectRVAdapter(val list: MutableList<inspectListItem>) : RecyclerView.Adapter<InspectRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_item, parent, false)

        return ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view : View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemClick != null) {
            holder.itemView.setOnClickListener { v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(list[position])
    }

    // 전체 리사이클러뷰의 개수
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item : inspectListItem) {
            val date = itemView.findViewById<TextView>(R.id.date)
            val part = itemView.findViewById<TextView>(R.id.part)
            val isdefected = itemView.findViewById<TextView>(R.id.isdefected)
            val isfixed = itemView.findViewById<TextView>(R.id.isfixed)

            date.text = item.date
            part.text = "부품명 : " + item.part

            if (item.isdefected)
                isdefected.text = "불량 : O"
            else
                isdefected.text = "불량 : X"

            if (item.isfixed)
                isfixed.text = "조치 상태 : O"
            else
                isfixed.text = "조치 상태 : X"

        }

    }


}