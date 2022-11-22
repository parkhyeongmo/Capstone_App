package com.capstone.frontapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InspectRVAdapter(type: Int) : RecyclerView.Adapter<InspectRVAdapter.ViewHolder>() {

    private var list = ArrayList<inspectListItem>()
    private var type = type

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
            val symbol = itemView.findViewById<ImageView>(R.id.image_result_symbol)
            
            date.text = item.date
            part.text = item.part

            // 사용자의 경우 결과 내역에 불량 여부를 체크와 X로 표시
            if (type == 0) {
                if (item.isdefected == 0) {
                    symbol.setImageResource(R.drawable.ic_baseline_done_24)                    
                }
                else {
                    symbol.setImageResource(R.drawable.ic_baseline_clear_24)
                }
            }
            
            // 담당자의 경우 결과 내역에 조치 여부를 체크와 X로 표시
            else if (type == 1) {
                if (item.isfixed== 0) {
                    symbol.setImageResource(R.drawable.ic_baseline_clear_24)
                }
                else {
                    symbol.setImageResource(R.drawable.ic_baseline_done_24)
                }
            }
           

        }

    }

}