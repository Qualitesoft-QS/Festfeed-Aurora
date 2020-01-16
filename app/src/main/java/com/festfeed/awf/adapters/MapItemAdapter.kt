package com.festfeed.awf.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festfeed.awf.R
import com.festfeed.awf.models.Location
import com.festfeed.awf.utils.click
import kotlinx.android.synthetic.main.map_item.view.*

class MapItemAdapter(private val itemList: List<Location>, val callBack :(pos: Int)-> Unit) :
    RecyclerView.Adapter<MapItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.map_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, callBack)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemPos: Int,callBack : (pos : Int) -> Unit) = with(itemView) {
            mapItemName.text = itemList[itemPos].name
            click { callBack(itemPos) }
        }
    }

}