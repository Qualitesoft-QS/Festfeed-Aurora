package com.festfeed.awf.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.festfeed.awf.R
import com.festfeed.awf.utils.click
import kotlinx.android.synthetic.main.item_date.view.*
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter(
    private val dateList: ArrayList<Calendar>,
    private val callBack: (pos: Int) -> Unit
) :
    RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    private val sdf = SimpleDateFormat("EEE", Locale.getDefault())

    var selectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dateList[position], position, callBack)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(date: Calendar, position: Int, callBack: (pos: Int) -> Unit) = with(itemView) {
            if (position == selectedPos) {
                dayText.setTextColor(Color.WHITE)
                dateText.setTextColor(Color.WHITE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rootLyt.backgroundTintList = ColorStateList.valueOf(itemView.context.getColor(R.color.darkSkyBlue))
                }else
                {
                    rootLyt.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#69B7FD"))
                }
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dayText.setTextColor(itemView.context.getColor(R.color.darkSkyBlue))
                    dateText.setTextColor(itemView.context.getColor(R.color.colorBlack))
                }else
                {
                    dayText.setTextColor(Color.parseColor("#69B7FD"))
                  //  dateText.setTextColor(Color.parseColor("#000000"))
                    dateText.setTextColor(Color.BLACK)
                }


                rootLyt.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F7F7F7"))
            }
            sdf.applyPattern("EEE")
            dayText.text = sdf.format(date.time)
            sdf.applyPattern("dd")
            dateText.text = sdf.format(date.time)

            click {
                selectedPos = position
                callBack(position)
                notifyDataSetChanged()
            }
        }
    }

}