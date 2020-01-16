package com.festfeed.awf.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.festfeed.awf.R
import com.festfeed.awf.adapters.DateAdapter
import com.festfeed.awf.adapters.EventItemAdapter
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.models.EventResponse
import com.festfeed.awf.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.activity_schedule.*
import java.util.*
import kotlin.collections.ArrayList


class ScheduleActivity : BaseActivity() {

    private val sortSheet by lazy { BottomSheetBehavior.from(sortingSheet) }
    private val eventSheet by lazy { BottomSheetBehavior.from(eventDetailSheet) }
   // private val sortItems = ArrayList<String>("Favourites", "Time")

    val sortItems: ArrayList<String> = ArrayList()
    var selectedDate : Calendar = Calendar.getInstance();

    private lateinit var selectedData: EventResponse
    override fun layoutResID() = R.layout.activity_schedule
    var hashMapLocationIdfromschedule_items : HashMap<String, String> = HashMap<String, String> ()
    var hasmapOfLocationName : HashMap<String, String> = HashMap<String, String> ()
    var selectedLocation="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        } catch (e: Exception) {
        }

        selectedData = Preferences.eventResponse.single {
            it.location == Preferences.locationId
        }
        val sortedList=  selectedData.schedule_items.sortedBy { it.start_time }

        selectedData.schedule_items.clear()

        selectedData.schedule_items.addAll(sortedList)
        sortItems.add("Favourites");
        sortItems.add("Time");

        selectedData.schedule_items.forEach {
            hashMapLocationIdfromschedule_items.put(it.location,"1");
            var locationID=it.location;
            selectedData.locations.forEach {

                if(it.id.equals(locationID))
                {
                    hasmapOfLocationName.put(it.name,locationID);
                }
            }
        }






            for(key in hasmapOfLocationName.keys){

                sortItems.add(key)
            }



        dateRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        eventRecyclerView.layoutManager = LinearLayoutManager(this)

        sortPicker.minValue = 0
        sortPicker.maxValue = sortItems.size - 1
        val array = arrayOfNulls<String>(sortItems. size)
        sortItems. toArray(array)

        sortPicker.displayedValues =   array //.toArray() as Array<out String>?

        sortBtn.click { sortSheet.setState(BottomSheetBehavior.STATE_EXPANDED) }
        doneBtn.click {
            sortSheet.state = BottomSheetBehavior.STATE_HIDDEN

            try {
                selectedLocation= hasmapOfLocationName.get(sortItems.get(sortPicker.value)).toString();
            } catch (e: Exception) {
            }

            if(sortPicker.value==0)
            {
                selectedLocation="0"
            }
           else  if(sortPicker.value==1)
            {
                selectedLocation="1"
            }

            (eventRecyclerView.adapter as EventItemAdapter).sortByFliterDataAccToSelection(sortPicker.value,selectedDate, selectedData.schedule_items
                ,selectedLocation)
        }
        backBtn.click { onBackPressed() }

        val dateList = getDates();


        dateRecyclerView.adapter = DateAdapter(dateList) {
            try {
                monthname.text = monthname.parseDate(dateList[it].timeInMillis / 1000, "MMM yyyy")

                selectedDate= dateList[it]
                (eventRecyclerView.adapter as EventItemAdapter).filterData(
                    dateList[it],
                    selectedData.schedule_items,selectedLocation
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        eventSheet.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    descText.text =""
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })


        eventRecyclerView.adapter = EventItemAdapter(selectedData.schedule_items, {
            eventSheet.state = BottomSheetBehavior.STATE_EXPANDED
            headingText.text = it.name

//            val imgUrl =
//                "https://api-dev.festfeed.com/schedule_item/".plus(it.id)
//                    .plus("/image")

            val imgUrl =
                "https://api-glow.festfeed.com/schedule_item/".plus(it.id)
                    .plus("/image")

            Glide.with(this).load(imgUrl).placeholder(R.drawable.christmas_back_splash)
                .into(eventImage)

            val id= it.id
            if (Preferences.favEventList.contains(it.id)) {
                favIcon_main.setImageResource(R.drawable.love_select)
            } else {
                favIcon_main.setImageResource(R.drawable.love_unselect)
            }


            favIcon_main.setOnClickListener {
                val list = Preferences.favEventList

                if (list.contains(id)) {
                    list.remove(id)
                    favIcon_main.setImageResource(R.drawable.love_unselect)
                } else {
                    list.add(id)
                    favIcon_main.setImageResource(R.drawable.love_select)
                }

                Preferences.favEventList = list
                eventRecyclerView.adapter?.notifyDataSetChanged()
            }

            startDateText.text = startDateText.parseDate(
                it.start_time,
                "MMM dd, YYYY"
            )
            timeText.text = timeText.parseDate(
                it.start_time,
                "hh:mm a"
            ).plus(" to ").plus(
                timeText.parseDate(
                    it.end_time,
                    "hh:mm a"
                )
            )
            descText.text = it.description

            val location =
                selectedData.locations.single { loc -> loc.id == it.location }

            locationText.text = location.name
            locationText.setOnClickListener {


                startActivity( intentTo(MapsActivityForSingle::class.java).putExtra(
                    "lat",
                    location.latitude
                ).putExtra(
                    "lng",
                    location.longitude
                ).putExtra(
                    "name",
                    location.name
                ))
            }

        }, {
            val list = Preferences.favEventList

            if (list.contains(it.id)) {
                list.remove(it.id)
            } else {
                list.add(it.id)
            }

            Preferences.favEventList = list
            eventRecyclerView.adapter?.notifyDataSetChanged()
        })

        try {
            if (dateList.size > 0)
            {
                selectedDate= dateList[0]
                (eventRecyclerView.adapter as EventItemAdapter).filterData(
                    dateList[0],
                    selectedData.schedule_items,selectedLocation
                )
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        finishDown()
    }

    private fun getDates(): ArrayList<Calendar> {
        val dateList = ArrayList<Calendar>()
        for (item in selectedData.schedule_items) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = item.start_time * 1000

            if (!hasDate(dateList, cal))
                dateList.add(cal)
        }

        return dateList
    }

    private fun hasDate(list: ArrayList<Calendar>, withDate: Calendar): Boolean {

        for (cal in list) {

            if (cal.get(Calendar.DAY_OF_YEAR) == withDate.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == withDate.get(Calendar.YEAR)
            ) {
                return true
            }
        }
        return false
    }

}