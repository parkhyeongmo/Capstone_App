package com.capstone.frontapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PartsListViewAdapter(val list : MutableList<part>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.count()
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var converView = p1

        if (converView == null) {
            converView = LayoutInflater.from(p2?.context).inflate(R.layout.partslist_item, p2, false)
        }

        var name = converView!!.findViewById<TextView>(R.id.partName)
        var stock = converView!!.findViewById<TextView>(R.id.partStock)

        name.text = list[p0].part_name
        stock.text = list[p0].stock.toString()

        return converView!!
    }
}