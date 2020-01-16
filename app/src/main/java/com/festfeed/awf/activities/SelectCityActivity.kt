package com.festfeed.awf.activities

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.festfeed.awf.R
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.models.EventResponse
import com.festfeed.awf.network.RetrofitClient
import com.festfeed.awf.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_select_city.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class  SelectCityActivity : BaseActivity() {

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(sheet)
    }

    private val cities = arrayListOf<String>()
    private val citiesIds = arrayListOf<String>()
    private val eventids = arrayListOf<String>()




    override fun layoutResID() = R.layout.activity_select_city

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

        selectedCity.click {
            if(citiesIds.size>0)
            {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }

        }
        doneBtn.click {
            Preferences.isLoggedIn = true
            selectedCity.isEnabled = false

            if(cities.size >0) {
                selectedCity.text = cities[cityPicker.value]
                Preferences.locationId = citiesIds[cityPicker.value]

                val eventId=Preferences.eventid;
                Preferences.eventid = eventids[cityPicker.value]

                if(eventId != eventids[cityPicker.value])
                {
                    val sharedPrefs = applicationContext
                        .getSharedPreferences("Festfeed", MODE_PRIVATE);
                    val editor = sharedPrefs.edit()
                    editor.clear()
                    editor.commit()
                }




                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                runWithDelay(500) { startActivityWithAlpha(intentTo(DashboardActivity::class.java), true) }
            }

        }
        dummyData()

        cityPicker.setWrapSelectorWheel(false);
        cityPicker.visibility=View.INVISIBLE
        setCites()
        fetchEvents()
        view_privacy_policy.setOnClickListener {
            startActivityWithUp(
                intentTo(WebViewActivity::class.java).putExtra(
                    "url",
                    "https://festfeed.com/privacy/"
                )
            )
        }
        view_the_terms_of_service_.setOnClickListener {
            startActivityWithUp(
                intentTo(WebViewActivity::class.java).putExtra(
                    "url",
                    "https://festfeed.com/terms-of-use/"
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        selectedCity.isEnabled = true
    }

    override fun onStart() {
        super.onStart()
    }

    private fun fetchEvents() {
        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getEvents(
            Preferences.auth
        ).enqueue(object : Callback<List<EventResponse>> {
            override fun onFailure(call: Call<List<EventResponse>>, t: Throwable) {
                Log.e("", "")
                progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<List<EventResponse>>,
                response: Response<List<EventResponse>>
            ) {
                Handler().postDelayed({
                    progressBar.visibility = View.GONE
                }, 3000)
                if (response.code() == 200) {
                    Preferences.eventResponse = response.body()!!

                    setCites();
                }
            }

        })
    }



    fun setCites()
    {

        try {
            selectedCity.isEnabled = false
            cities.clear()
            citiesIds.clear()
            eventids.clear()
            for (event in Preferences.eventResponse) {

                cities.add(event.locations[0].city)
                citiesIds.add(event.locations[0].id)
                eventids.add(event.id)


            }
            if(cities.size>0){
                cityPicker.visibility=View.VISIBLE
                cityPicker.invalidate()
                cityPicker.minValue = 0
                cityPicker.maxValue = cities.size - 1

                val array = arrayOfNulls<String>(cities.size)
                cityPicker.displayedValues = cities.toArray(array)
                progressBar.visibility = View.GONE
                selectedCity.isEnabled = true
            }

        } catch (e: Exception) {
        }

    }

    fun dummyData()
    {
        if(Preferences.eventResponse.size==0)
        {
            var dummYdata= "[{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":1,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.dk/en/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://odensesupport.glowgardens.com/hc/en-us\"}],\"color\":\"191B33\",\"created\":1573239109,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: November 21 - January 5\\nCheck our website for daily hours of operation www.glowgardens.com/\\n\",\"end_time\":1578160800,\"id\":\"0e0e2b7d-76a0-465d-8876-a015f28e71e2\",\"location\":\"22d26528-cd62-4d37-af1b-8ca2d346cab0\",\"locations\":[{\"city\":\"Odense SV\",\"created\":1573239109,\"id\":\"22d26528-cd62-4d37-af1b-8ca2d346cab0\",\"latitude\":55.35673904418945,\"longitude\":10.356606483459473,\"name\":\"Main Entrance\",\"state\":\"Southern Denmark\"},{\"city\":\"Odense SV\",\"created\":1573675587,\"id\":\"28376302-247f-4b36-bcd2-217a885ece7e\",\"latitude\":55.356990814208984,\"longitude\":10.355998039245605,\"name\":\"Stage\",\"state\":\"Southern Denmark\"},{\"city\":\"Odense SV\",\"created\":1573675638,\"id\":\"72f09bb9-5474-4f8d-a1d9-72ef28c8d0af\",\"latitude\":55.356807708740234,\"longitude\":10.355914115905762,\"name\":\"Vendor Market\",\"state\":\"Southern Denmark\"},{\"city\":\"Odense SV\",\"created\":1573675695,\"id\":\"4a913ce1-bad5-4bd7-a369-9ffb357d6a14\",\"latitude\":55.35650634765625,\"longitude\":10.356535911560059,\"name\":\"Food Trucks\",\"state\":\"Southern Denmark\"},{\"city\":\"Odense SV\",\"created\":1573677379,\"id\":\"2359e545-0c07-46bc-97cc-53e54a06d686\",\"latitude\":55.35718536376953,\"longitude\":10.355917930603027,\"name\":\"Santas Sleigh\",\"state\":\"Southern Denmark\"}],\"name\":\"Christmas Glow Odense\",\"published\":true,\"schedule_items\":[{\"created\":1573239109,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1574974800,\"id\":\"de131d2e-7492-4565-b4ea-c7b54ce6d732\",\"location\":\"22d26528-cd62-4d37-af1b-8ca2d346cab0\",\"name\":\"Event Starts\",\"start_time\":1574953200},{\"created\":1573239110,\"description\":\"Come listen to some amazing artists at our live stage\",\"end_time\":1574958600,\"id\":\"1216640d-fb76-4a77-ad74-479374fc315b\",\"location\":\"22d26528-cd62-4d37-af1b-8ca2d346cab0\",\"name\":\"Tony Quinn\",\"start_time\":1574955000}],\"start_time\":1574348400,\"timezone\":\"UTC\"},{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":2,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.com/toronto-christmas/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://glowtoronto.zendesk.com/hc/en-us/\"}],\"color\":\"191B33\",\"created\":1573239115,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: November 21 - January 5\\nCheck our website for daily hours of operation www.glowgardens.com/\\n\",\"end_time\":1578182400,\"id\":\"8a55aafe-6bac-4941-a7b9-5ef8fdd96eb2\",\"location\":\"f2477edd-c8ca-48a0-816d-dc676d2a64ca\",\"locations\":[{\"city\":\"Toronto\",\"created\":1573239115,\"id\":\"f2477edd-c8ca-48a0-816d-dc676d2a64ca\",\"latitude\":43.694801330566406,\"longitude\":-79.5762710571289,\"name\":\"Main Entrance\",\"state\":\"ON\"},{\"city\":\"Toronto\",\"created\":1573678898,\"id\":\"bfe4c88f-05f6-4f7c-b37d-e9e6591b1ae5\",\"latitude\":43.694705963134766,\"longitude\":-79.57721710205078,\"name\":\"Washrooms\",\"state\":\"ON\"},{\"city\":\"Toronto\",\"created\":1573678955,\"id\":\"86c6f70b-04ae-42e3-a732-a605551e4a94\",\"latitude\":43.694801330566406,\"longitude\":-79.57703399658203,\"name\":\"Food Vendors\",\"state\":\"ON\"},{\"city\":\"Toronto\",\"created\":1573679034,\"id\":\"17c016ab-8ed8-4984-b878-c2175fbb744d\",\"latitude\":43.69502639770508,\"longitude\":-79.57649993896484,\"name\":\"Train Station\",\"state\":\"ON\"},{\"city\":\"Toronto\",\"created\":1573679123,\"id\":\"bf5d38f7-9c6a-447e-92c7-694571b110a4\",\"latitude\":43.695186614990234,\"longitude\":-79.57688903808594,\"name\":\"Stage\",\"state\":\"ON\"},{\"city\":\"Toronto\",\"created\":1573679222,\"id\":\"d2e14f59-0d45-4eb1-82d9-3b6edfb7a67e\",\"latitude\":43.695247650146484,\"longitude\":-79.5777816772461,\"name\":\"Movie Room\",\"state\":\"ON\"}],\"name\":\"Christmas Glow Toronto\",\"published\":true,\"schedule_items\":[{\"created\":1573239116,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1575082800,\"id\":\"1663bacb-3ec5-4b38-9952-552f30c5d08e\",\"location\":\"f2477edd-c8ca-48a0-816d-dc676d2a64ca\",\"name\":\"Event Starts\",\"start_time\":1575061200},{\"created\":1573239116,\"description\":\"Come listen to some amazing artists at our live stage\",\"end_time\":1575066600,\"id\":\"4b1fb1b5-2fb0-4d75-b404-dcdf1a74f1a0\",\"location\":\"f2477edd-c8ca-48a0-816d-dc676d2a64ca\",\"name\":\"Tony Quinn\",\"start_time\":1575063000}],\"start_time\":1574974800,\"timezone\":\"UTC\"},{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":2,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.com/edmonton-christmas/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://edmontonsupport.glowgardens.com/hc/en-us\"}],\"color\":\"191B33\",\"created\":1573239106,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: November 21 - January 5\\nCheck our website for daily hours of operation www.glowgardens.com/\\n\",\"end_time\":1578189600,\"id\":\"9bb70c83-0196-4e3a-8954-db63c6dacd2e\",\"location\":\"f26db781-f10d-48f2-b8af-4d2f9538f299\",\"locations\":[{\"city\":\"Edmonton\",\"created\":1573239106,\"id\":\"f26db781-f10d-48f2-b8af-4d2f9538f299\",\"latitude\":53.568321228027344,\"longitude\":-113.45824432373047,\"name\":\"Main Entrance\",\"state\":\"AB\"},{\"city\":\"Edmonton\",\"created\":1573455427,\"id\":\"e755330c-02d6-4e8b-8787-772422230b9e\",\"latitude\":53.56820297241211,\"longitude\":-113.45882415771484,\"name\":\"Entertainment Stage\",\"state\":\"AB\"},{\"city\":\"Edmonton\",\"created\":1573455633,\"id\":\"8884144f-487b-4727-9bcd-f2229d657018\",\"latitude\":53.568058013916016,\"longitude\":-113.45838165283203,\"name\":\"Food Vendors\",\"state\":\"AB\"},{\"city\":\"Edmonton\",\"created\":1573673379,\"id\":\"69c04280-82db-40ac-829b-116d255e95be\",\"latitude\":53.56854248046875,\"longitude\":-113.45880126953125,\"name\":\"Exit\",\"state\":\"AB\"},{\"city\":\"Edmonton\",\"created\":1573673469,\"id\":\"1c436870-7851-446b-9b7c-37e956e23851\",\"latitude\":53.568031311035156,\"longitude\":-113.45962524414062,\"name\":\"Movie Room\",\"state\":\"AB\"},{\"city\":\"Edmonton\",\"created\":1573673540,\"id\":\"f382592d-f686-4890-a2ce-3780684879f0\",\"latitude\":53.568321228027344,\"longitude\":-113.4583969116211,\"name\":\"Info Desk\",\"state\":\"AB\"}],\"name\":\"Christmas Glow Edmonton\",\"published\":true,\"schedule_items\":[{\"created\":1573239106,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1575003600,\"id\":\"a33f9b72-95e0-48c3-90e2-056bb9492a7a\",\"location\":\"f26db781-f10d-48f2-b8af-4d2f9538f299\",\"name\":\"Event Starts\",\"start_time\":1574982000}],\"start_time\":1574982000,\"timezone\":\"UTC\"},{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":7,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.com/vancouver-christmas/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://vancouversupport.glowgardens.com/hc/en-us\"}],\"color\":\"191B33\",\"created\":1573239116,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: November 21 - January 5\\nCheck our website for daily hours of operation www.glowgardens.com/vancouver-christmas/\\n\\nHarbour Convention Centre, Vancouver\",\"end_time\":1578279600,\"id\":\"9bfbc013-46c9-42a0-8e7d-5b9168f450cb\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"locations\":[{\"city\":\"Vancouver\",\"created\":1573239116,\"id\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"latitude\":49.274694,\"longitude\":-123.109568,\"name\":\"Main Entrance\",\"state\":\"BC\"},{\"city\":\"Vancouver\",\"created\":1573239116,\"id\":\"eba35403-deee-405c-8ac5-a02f8f143efc\",\"latitude\":49.274478,\"longitude\":-123.109499,\"name\":\"Stage\",\"state\":\"BC\"},{\"city\":\"Vancouver\",\"created\":1573239116,\"id\":\"bbbcf29c-4f65-4a83-a0c7-ee8a15e6eef9\",\"latitude\":49.274718,\"longitude\":-123.1094,\"name\":\"Food Vendors\",\"state\":\"BC\"},{\"city\":\"Vancouver\",\"created\":1573239116,\"id\":\"c24c27a4-fbc4-41f3-b267-435695418f5c\",\"latitude\":49.27486038208008,\"longitude\":-123.10940551757812,\"name\":\"Washrooms\",\"state\":\"BC\"},{\"city\":\"Vancouver\",\"created\":1573239116,\"id\":\"670965e0-dbe7-49c8-a9b6-1ba8d3852583\",\"latitude\":49.274593,\"longitude\":-123.109057,\"name\":\"Train Station\",\"state\":\"BC\"}],\"name\":\"Christmas Glow Vancouver\",\"published\":true,\"schedule_items\":[{\"created\":1573239117,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1574402400,\"id\":\"a97a7f3b-92da-4e15-9cbb-2c639dc9f8c8\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Event Starts\",\"start_time\":1574380800},{\"created\":1573239117,\"description\":\"Come listen to the amazing voice of Tony Quinn at our live stage\",\"end_time\":1574386200,\"id\":\"c27236d5-fe00-4121-9f98-3fec60d81941\",\"location\":\"eba35403-deee-405c-8ac5-a02f8f143efc\",\"name\":\"Tony Quinn\",\"start_time\":1574382600},{\"created\":1573239117,\"description\":\"In this area a description of the above schedule item will be inserted to help the user know what they should expect from this item\",\"end_time\":1574389800,\"id\":\"519be38d-6215-43f0-899f-ed9801895b37\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Santa Show\",\"start_time\":1574384400},{\"created\":1573239118,\"description\":\"In this area a description of the above schedule item will be inserted to help the user know what they should expect from this item\",\"end_time\":1574394300,\"id\":\"bd244c43-4bf7-47d2-a9d5-33b64c34bf60\",\"location\":\"eba35403-deee-405c-8ac5-a02f8f143efc\",\"name\":\"Tom Collins Band\",\"start_time\":1574391600},{\"created\":1573239118,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1574488800,\"id\":\"3367cfe7-17f5-43c6-b860-5fbc8ea0a076\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Event Starts\",\"start_time\":1574467200},{\"created\":1573239120,\"description\":\"In this area a description of the above schedule item will be inserted to help the user know what they should expect from this item\",\"end_time\":1574476200,\"id\":\"dedee3d4-1c13-461f-ade0-f1bd3e22acf3\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Santa Show\",\"start_time\":1574470800},{\"created\":1573239120,\"description\":\"In this area a description of the above schedule item will be inserted to help the user know what they should expect from this item\",\"end_time\":1574480700,\"id\":\"f21ac0bb-bf0e-4eb3-b071-2d12d5a08cce\",\"location\":\"eba35403-deee-405c-8ac5-a02f8f143efc\",\"name\":\"Tom Collins Band\",\"start_time\":1574478000},{\"created\":1573239121,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1574575200,\"id\":\"f8c95264-bed0-4ebd-b564-43d73f14184c\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Event Starts\",\"start_time\":1574553600},{\"created\":1573239121,\"description\":\"Come listen to the amazing voice of Tony Quinn at our live stage\",\"end_time\":1574559000,\"id\":\"d28e6482-80e6-41cd-93c3-88c8457f05db\",\"location\":\"eba35403-deee-405c-8ac5-a02f8f143efc\",\"name\":\"Tony Quinn\",\"start_time\":1574555400},{\"created\":1573239122,\"description\":\"In this area a description of the above schedule item will be inserted to help the user know what they should expect from this item\",\"end_time\":1574562600,\"id\":\"99143958-c9df-4559-8f93-2f407dde5ed0\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Santa Show\",\"start_time\":1574557200},{\"created\":1573239122,\"description\":\"In this area a description of the above schedule item will be inserted to help the user know what they should expect from this item\",\"end_time\":1574567100,\"id\":\"9f0ec9f7-0f30-45d3-8759-f9c5ac0eb597\",\"location\":\"eba35403-deee-405c-8ac5-a02f8f143efc\",\"name\":\"Tom Collins Band\",\"start_time\":1574564400},{\"created\":1573239123,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1574661600,\"id\":\"d40c99ab-1821-4614-8dc3-2b38747e7776\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Event Starts\",\"start_time\":1574640000},{\"created\":1573239123,\"description\":\"Come listen to the amazing voice of Tony Quinn at our live stage\",\"end_time\":1574645400,\"id\":\"e5d64c00-697b-45b8-9a56-f5b7a16a343a\",\"location\":\"eba35403-deee-405c-8ac5-a02f8f143efc\",\"name\":\"Tony Quinn\",\"start_time\":1574641800},{\"created\":1573239123,\"description\":\"In this area a description of the above schedule item will be inserted to help the user know what they should expect from this item\",\"end_time\":1574649000,\"id\":\"998848ef-6a6d-4a47-a90b-84d68c058517\",\"location\":\"95ca5027-f213-4e88-aa68-8156e23c9032\",\"name\":\"Santa Show\",\"start_time\":1574643600}],\"start_time\":1574380800,\"timezone\":\"UTC\"},{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":2,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.com/saskatoon-christmas/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://saskatoonsupport.glowgardens.com/hc/en-us\"}],\"color\":\"191B33\",\"created\":1573239112,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: November 21 - January 5\\nCheck our website for daily hours of operation www.glowgardens.com/\\n\",\"end_time\":1577581200,\"id\":\"cb99cb83-7b9e-4631-859f-995189edbc42\",\"location\":\"66bb263d-9dc4-4d10-8d49-c168dbc9f0f7\",\"locations\":[{\"city\":\"Saskatoon\",\"created\":1573239112,\"id\":\"66bb263d-9dc4-4d10-8d49-c168dbc9f0f7\",\"latitude\":52.098437,\"longitude\":-106.67763,\"name\":\"Main Entrance\",\"state\":\"SK\"},{\"city\":\"Saskatoon\",\"created\":1573678162,\"id\":\"03c97821-97c8-4ceb-ad94-24519f6721da\",\"latitude\":52.09858322143555,\"longitude\":-106.67742919921875,\"name\":\"Washrooms\",\"state\":\"SK\"},{\"city\":\"Saskatoon\",\"created\":1573678207,\"id\":\"82c510be-947f-4620-93cf-6e9508aad906\",\"latitude\":52.098873138427734,\"longitude\":-106.67725372314453,\"name\":\"Stage\",\"state\":\"SK\"},{\"city\":\"Saskatoon\",\"created\":1573678310,\"id\":\"cfff9548-8de1-4552-87d8-edd6489a882b\",\"latitude\":52.0986328125,\"longitude\":-106.6772232055664,\"name\":\"Info\",\"state\":\"SK\"},{\"city\":\"Saskatoon\",\"created\":1573678371,\"id\":\"55c996c4-790e-4893-b57a-c98424b2ecb0\",\"latitude\":52.09865951538086,\"longitude\":-106.67700958251953,\"name\":\"Food\",\"state\":\"SK\"},{\"city\":\"Saskatoon\",\"created\":1573678454,\"id\":\"08cbddfb-ec52-4495-9a6f-50676dc9e92b\",\"latitude\":52.09908676147461,\"longitude\":-106.67735290527344,\"name\":\"Letter Writing\",\"state\":\"SK\"},{\"city\":\"Saskatoon\",\"created\":1573678539,\"id\":\"f7b26157-a6e2-49cc-89b2-48fcf0c01052\",\"latitude\":52.09914016723633,\"longitude\":-106.67700958251953,\"name\":\"Movie Room\",\"state\":\"SK\"}],\"name\":\"Christmas Glow Saskatoon\",\"published\":true,\"schedule_items\":[{\"created\":1573239113,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1575000000,\"id\":\"0da7e2a6-48ec-4e98-b7e8-506408787074\",\"location\":\"66bb263d-9dc4-4d10-8d49-c168dbc9f0f7\",\"name\":\"Event Starts\",\"start_time\":1574978400},{\"created\":1573239113,\"description\":\"Come listen to some amazing artists at our live stage\",\"end_time\":1574983800,\"id\":\"ced9930c-96b5-4f20-b7e6-05037978da17\",\"location\":\"66bb263d-9dc4-4d10-8d49-c168dbc9f0f7\",\"name\":\"Tony Quinn\",\"start_time\":1574980200}],\"start_time\":1574373600,\"timezone\":\"UTC\"},{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":2,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.com/halifax-christmas/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://halifaxsupport.glowgardens.com/hc/en-us\"}],\"color\":\"191B33\",\"created\":1573239107,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: November 27 - January 4\\nCheck our website for daily hours of operation www.glowgardens.com/\\n\",\"end_time\":1578178800,\"id\":\"d0b542d9-008c-4138-972b-fdb423dcf029\",\"location\":\"336ed17e-ac66-4624-8af6-f6dbb11e5fd7\",\"locations\":[{\"city\":\"Halifax\",\"created\":1573239107,\"id\":\"336ed17e-ac66-4624-8af6-f6dbb11e5fd7\",\"latitude\":44.61769104003906,\"longitude\":-63.665077209472656,\"name\":\"Main Entrance\",\"state\":\"NS\"},{\"city\":\"Halifax\",\"created\":1573675184,\"id\":\"990ac36d-d8d2-487b-9fe0-ec3fe04366ff\",\"latitude\":44.617156982421875,\"longitude\":-63.664695739746094,\"name\":\"Entertainment Stage\",\"state\":\"NS\"},{\"city\":\"Halifax\",\"created\":1573675343,\"id\":\"bc3f781d-bcd6-472e-8734-e3675bbb1478\",\"latitude\":44.61650085449219,\"longitude\":-63.66505813598633,\"name\":\"Train Station\",\"state\":\"NS\"},{\"city\":\"Halifax\",\"created\":1573679497,\"id\":\"c8d88776-6db9-471b-b8a1-ea375ca51dc3\",\"latitude\":44.616886138916016,\"longitude\":-63.66495895385742,\"name\":\"Info Desk\",\"state\":\"NS\"},{\"city\":\"Halifax\",\"created\":1573679571,\"id\":\"98070588-01a6-4df1-89a4-efbf49d7ba14\",\"latitude\":44.616981506347656,\"longitude\":-63.66438674926758,\"name\":\"Movie Room\",\"state\":\"NS\"}],\"name\":\"Christmas Glow Halifax\",\"published\":true,\"schedule_items\":[{\"created\":1573239108,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1574992800,\"id\":\"3187063f-16b5-4be4-af24-838aeba09796\",\"location\":\"336ed17e-ac66-4624-8af6-f6dbb11e5fd7\",\"name\":\"Event Starts\",\"start_time\":1574971200},{\"created\":1573239108,\"description\":\"Come listen to some amazing artists at our live stage\",\"end_time\":1574976600,\"id\":\"e7fadcba-3f53-451e-87ec-d1c6d1d44eba\",\"location\":\"336ed17e-ac66-4624-8af6-f6dbb11e5fd7\",\"name\":\"Tony Quinn\",\"start_time\":1574973000}],\"start_time\":1574884800,\"timezone\":\"UTC\"},{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":5,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.com/abbotsford-christmas/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://abbotsfordsupport.glowgardens.com/hc/en-us\"}],\"color\":\"191B33\",\"created\":1573239105,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: December 5  - January 5\\n\\nCheck our website for daily hours of operation www.glowgardens.com/\\n\",\"end_time\":1578279600,\"id\":\"d3aa1255-0261-48d7-88fb-582ce42e9f22\",\"location\":\"7971d368-1413-43c3-a804-1c4daa48f1f5\",\"locations\":[{\"city\":\"Abbotsford\",\"created\":1573239105,\"id\":\"7971d368-1413-43c3-a804-1c4daa48f1f5\",\"latitude\":49.02427291870117,\"longitude\":-122.38204193115234,\"name\":\"Main Entrance\",\"state\":\"BC\"},{\"city\":\"Abbotsford\",\"created\":1573454355,\"id\":\"69bc02a1-56c0-44de-8b95-26fa03784602\",\"latitude\":49.023895263671875,\"longitude\":-122.38103485107422,\"name\":\"Entertainment Stage\",\"state\":\"BC\"},{\"city\":\"Abbotsford\",\"created\":1573454771,\"id\":\"9a06d971-9a8b-4af7-a63f-45a1127b0dd1\",\"latitude\":49.0239372253418,\"longitude\":-122.38153076171875,\"name\":\"Food\",\"state\":\"BC\"},{\"city\":\"Abbotsford\",\"created\":1573455038,\"id\":\"d66706da-2a2c-40ad-a02e-8f561d26fae1\",\"latitude\":49.0242919921875,\"longitude\":-122.38116455078125,\"name\":\"Washrooms\",\"state\":\"BC\"},{\"city\":\"Abbotsford\",\"created\":1573665602,\"id\":\"cf36018e-f8af-46d4-8108-02d97e324287\",\"latitude\":49.02458190917969,\"longitude\":-122.38054656982422,\"name\":\"Train Station\",\"state\":\"BC\"},{\"city\":\"Abbotsford\",\"created\":1573667787,\"id\":\"1c9a165e-9d64-4bfe-8598-ff1e5d7e58ec\",\"latitude\":49.024051666259766,\"longitude\":-122.3819351196289,\"name\":\"Info Desk\",\"state\":\"BC\"}],\"name\":\"Christmas Glow Abbotsford\",\"published\":true,\"schedule_items\":[{\"created\":1573239105,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1575612000,\"id\":\"b8d612a9-3364-4033-9c00-c45c7c9fad4a\",\"location\":\"7971d368-1413-43c3-a804-1c4daa48f1f5\",\"name\":\"Event Starts\",\"start_time\":1575590400},{\"created\":1573239105,\"description\":\"Come listen to some amazing artists at our live stage\",\"end_time\":1575595800,\"id\":\"7ce8b23e-b044-40f1-9d05-7f6e4e30a18d\",\"location\":\"7971d368-1413-43c3-a804-1c4daa48f1f5\",\"name\":\"Tony Quinn\",\"start_time\":1575592200}],\"start_time\":1575590400,\"timezone\":\"UTC\"},{\"admins\":[\"d785e0c3-c7a3-4449-b133-18153d66b59e\"],\"attendees\":0,\"buttons\":[{\"image\":\"EventInfo\",\"name\":\"Info\",\"position\":0,\"url\":\"\"},{\"image\":\"EventSchedule\",\"name\":\"Schedule\",\"position\":1,\"url\":\"\"},{\"image\":\"EventMap\",\"name\":\"Map\",\"position\":2,\"url\":\"\"},{\"image\":\"EventTickets\",\"name\":\"Tickets\",\"position\":3,\"url\":\"https://www.glowgardens.com/ottawa-christmas/#calendar\"},{\"image\":\"EventGame\",\"name\":\"Game\",\"position\":4,\"url\":\"\"},{\"image\":\"EventVendors\",\"name\":\"Vendor\",\"position\":5,\"url\":\"https://vendors.festfeed.com/vancouver/\"},{\"image\":\"EventFAQ\",\"name\":\"FAQ\",\"position\":6,\"url\":\"https://ottawasupport.glowgardens.com/hc/en-us\"}],\"color\":\"191B33\",\"created\":1573239110,\"description\":\"Welcome to Glow!\\n\\nBring together the people you love to stroll, laugh and play under the twinkle of half a million lights.\\n\\nOur playgrounds and interactive features will entertain the kids for hours, while Glow’s illuminated sculptures are the perfect backdrop for selfies and family photos.\\n\\nIt’s all the magic and wonder of an outdoor seasonal festival, hosted inside our cozy and spacious venue.\\n\\nChristmas Glow: December 5 - January 5\\nCheck our website for daily hours of operation www.glowgardens.com/\\n\",\"end_time\":1578182400,\"id\":\"e4148085-5496-41a9-94db-b6885f440938\",\"location\":\"7d55e1e5-3d21-41af-aa1f-33906c15d0c2\",\"locations\":[{\"city\":\"Ottawa\",\"created\":1573239110,\"id\":\"7d55e1e5-3d21-41af-aa1f-33906c15d0c2\",\"latitude\":45.331459045410156,\"longitude\":-75.65369415283203,\"name\":\"Main Entrance\",\"state\":\"ON\"},{\"city\":\"Ottawa\",\"created\":1573677607,\"id\":\"3dd07b5d-b7e1-47d6-85ff-1574ac0922c7\",\"latitude\":45.33150100708008,\"longitude\":-75.65362548828125,\"name\":\"Info Desk\",\"state\":\"ON\"},{\"city\":\"Ottawa\",\"created\":1573677660,\"id\":\"8a46ca13-6ddb-4bb0-92e9-95871c16145f\",\"latitude\":45.33140182495117,\"longitude\":-75.65319061279297,\"name\":\"Stage\",\"state\":\"ON\"},{\"city\":\"Ottawa\",\"created\":1573677733,\"id\":\"db7ef86d-ca6e-49dd-b4a3-e93a5a171d60\",\"latitude\":45.33177947998047,\"longitude\":-75.65320587158203,\"name\":\"Food Vendors\",\"state\":\"ON\"},{\"city\":\"Ottawa\",\"created\":1573677820,\"id\":\"19f18455-25e2-4efc-b3f1-8898901fba43\",\"latitude\":45.331417083740234,\"longitude\":-75.65342712402344,\"name\":\"Bouncy Farm\",\"state\":\"ON\"},{\"city\":\"Ottawa\",\"created\":1573677908,\"id\":\"11df92dd-a415-42c2-840c-f1464066a333\",\"latitude\":45.33177947998047,\"longitude\":-75.65372467041016,\"name\":\"Train Station\",\"state\":\"ON\"}],\"name\":\"Christmas Glow Ottawa\",\"published\":true,\"schedule_items\":[{\"created\":1573239110,\"description\":\"The event has started! we cant wait to see each one of you\",\"end_time\":1575007200,\"id\":\"b38a3a33-ee60-450f-81ae-e93f0a019aa2\",\"location\":\"7d55e1e5-3d21-41af-aa1f-33906c15d0c2\",\"name\":\"Event Starts\",\"start_time\":1574985600},{\"created\":1573239111,\"description\":\"Come listen to some amazing artists at our live stage\",\"end_time\":1574991000,\"id\":\"26d14160-a982-4235-8c0e-186a1bcf1582\",\"location\":\"7d55e1e5-3d21-41af-aa1f-33906c15d0c2\",\"name\":\"Tony Quinn\",\"start_time\":1574987400}],\"start_time\":1575579600,\"timezone\":\"UTC\"}]"
            val gson = GsonBuilder().setPrettyPrinting().create()
            println("=== List from JSON ===")
            var personList: List<EventResponse> = gson.fromJson(dummYdata, object : TypeToken<List<EventResponse>>() {}.type)
            Preferences.eventResponse = personList
            Log.e("",""+Preferences.eventResponse)
            setCites()
        }

    }
}


