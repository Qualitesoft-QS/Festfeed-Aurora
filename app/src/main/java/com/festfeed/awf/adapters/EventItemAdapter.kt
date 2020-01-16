package com.festfeed.awf.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festfeed.awf.R
import com.festfeed.awf.models.ScheduleItem
import com.festfeed.awf.utils.Preferences
import com.festfeed.awf.utils.click
import com.festfeed.awf.utils.parseDate
import kotlinx.android.synthetic.main.activity_schedule.*
import kotlinx.android.synthetic.main.item_event.*
import kotlinx.android.synthetic.main.item_event.view.*
import java.util.*
import kotlin.collections.ArrayList

class EventItemAdapter(
    private var itemList: List<ScheduleItem>,
    private val clickCallback: (pos: ScheduleItem) -> Unit,
    private val callBackFav: (pos: ScheduleItem) -> Unit
) :
    RecyclerView.Adapter<EventItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, clickCallback, callBackFav)
    }

    override fun getItemCount(): Int {
        return itemList.size;
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pos: Int, clickCallback: (ScheduleItem) -> Unit, callBackFav: (ScheduleItem) -> Unit) =
            with(itemView) {
                itemTimeText.text =
                    parseDate(
                        itemList[pos].start_time,
                        "hh:mm\na"
                    )
                this.eventText.text = itemList[pos].name
                click { clickCallback(itemList.get(pos)) }

                if (Preferences.favEventList.contains(itemList[pos].id)) {
                    favIcon.setImageResource(R.drawable.love_select)
                } else {
                    favIcon.setImageResource(R.drawable.love_unselect)
                }

                favIcon.click { callBackFav(itemList.get(pos)) }
            }
    }

    fun filterData(calender: Calendar, originalList: List<ScheduleItem>,locationID: String) {
        val filteredList: ArrayList<ScheduleItem> = ArrayList()
        for (item in originalList) {

            val withDate = Calendar.getInstance()
            withDate.timeInMillis = item.start_time * 1000

            if (calender.get(Calendar.DAY_OF_YEAR) == withDate.get(Calendar.DAY_OF_YEAR) &&
                calender.get(Calendar.YEAR) == withDate.get(Calendar.YEAR)
            ) {

                if(locationID=="0")
                {
                    if(Preferences.favEventList.contains(item.id))
                    {
                        filteredList.add(item)
                    }
                }
                else if(locationID=="1")
                {
                    filteredList.add(item)
                }
               else  if(locationID.isNullOrBlank())
                {
                    filteredList.add(item)
                }
                else  if(locationID.equals(item.location)){
                    filteredList.add(item)
                }

            }
        }

        itemList = filteredList
        if(locationID.equals("1"))
        {
            itemList = itemList.sortedWith(compareBy {
                it.start_time
            })
        }
        notifyDataSetChanged()
    }

//    fun sortBy(type: Int) {
//        when (type) {
//
//            0 -> {
//                itemList = itemList.sortedWith(compareByDescending {
//                    Preferences.favEventList.contains(it.id)
//                })
//
//            }
//            1 -> {
//                itemList = itemList.sortedWith(compareBy {
//                    it.start_time
//                })
//            }
//            2 -> {
//
//            }
//
//        }
//        itemList.forEach { println(it.name) }
//        notifyDataSetChanged()
//    }



    fun sortByFliterDataAccToSelection(type: Int,calender: Calendar, originalList: List<ScheduleItem>, locationID:String ) {
        when (type) {

            0 -> {
                val filteredList: ArrayList<ScheduleItem> = ArrayList()
                for (item in originalList) {

                    val withDate = Calendar.getInstance()
                    withDate.timeInMillis = item.start_time * 1000

                    if (calender.get(Calendar.DAY_OF_YEAR) == withDate.get(Calendar.DAY_OF_YEAR) &&
                        calender.get(Calendar.YEAR) == withDate.get(Calendar.YEAR)
                    ) {
                        if(Preferences.favEventList.contains(item.id))
                        {
                            filteredList.add(item)
                        }

                    }
                }
                itemList = filteredList
                notifyDataSetChanged()

            }
            1 -> {
                val filteredList: ArrayList<ScheduleItem> = ArrayList()
                for (item in originalList) {

                    val withDate = Calendar.getInstance()
                    withDate.timeInMillis = item.start_time * 1000

                    if (calender.get(Calendar.DAY_OF_YEAR) == withDate.get(Calendar.DAY_OF_YEAR) &&
                        calender.get(Calendar.YEAR) == withDate.get(Calendar.YEAR)
                    ) {
                        filteredList.add(item)
                    }
                }
                itemList = filteredList

                itemList = itemList.sortedWith(compareBy {
                    it.start_time
                })

                notifyDataSetChanged()
            }
            else -> {

                val filteredList: ArrayList<ScheduleItem> = ArrayList()
                for (item in originalList) {

                    val withDate = Calendar.getInstance()
                    withDate.timeInMillis = item.start_time * 1000

                    if (calender.get(Calendar.DAY_OF_YEAR) == withDate.get(Calendar.DAY_OF_YEAR) &&
                        calender.get(Calendar.YEAR) == withDate.get(Calendar.YEAR)
                    ) {
                        if(locationID.equals(item.location))
                        {
                            filteredList.add(item)
                        }

                    }
                }
                itemList = filteredList
                notifyDataSetChanged()

            }

        }
        itemList.forEach { println(it.name) }
        notifyDataSetChanged()
    }


}