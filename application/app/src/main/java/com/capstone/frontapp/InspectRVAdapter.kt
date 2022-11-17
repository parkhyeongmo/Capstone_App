package com.capstone.frontapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InspectRVAdapter() : RecyclerView.Adapter<InspectRVAdapter.ViewHolder>() {

    private var list = ArrayList<inspectListItem>()

    fun setList(list: ArrayList<inspectListItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_item, parent, false)
        return ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, testId: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position, list[position].testid)
            }
        }
        holder.bindItems(list[position])
    }

    // 전체 리사이클러뷰의 개수
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {

            }
        }

        fun bindItems(item : inspectListItem) {
            val date = itemView.findViewById<TextView>(R.id.date)
            val part = itemView.findViewById<TextView>(R.id.part)
            val isdefected = itemView.findViewById<TextView>(R.id.isdefected)
            val isfixed = itemView.findViewById<TextView>(R.id.isfixed)

            date.text = item.date

            if (item.isdefected == 1) {
                isdefected.text = "불량 : O"
                part.text = item.part
                if (item.isfixed == 1) {
                    isfixed.text = "조치 : O"
                }
                else {
                    isfixed.text = "조치 : X"
                }

            }
            else {
                isdefected.text = "불량 : X"
                part.text = " "
                isfixed.text = " "
            }

        }

    }


}