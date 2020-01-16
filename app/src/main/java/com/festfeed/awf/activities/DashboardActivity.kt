package com.festfeed.awf.activities

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.festfeed.awf.R
import com.festfeed.awf.activities.gamescan.EventQuest
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.customviews.ButtonView
import com.festfeed.awf.gamemodel.GameData
import com.festfeed.awf.models.Button
import com.festfeed.awf.models.EventResponse
import com.festfeed.awf.network.RetrofitClient
import com.festfeed.awf.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class DashboardActivity : BaseActivity() {

    override fun layoutResID() = R.layout.activity_dashboard
    internal lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        pref = applicationContext.getSharedPreferences("Festfeed", MODE_PRIVATE)

        try {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        } catch (e: Exception) {
        }


        try {
            val obj = JSONObject(readJSONFromAsset()!!)
            val parser = JsonParser()
            val mJson = parser.parse(obj.toString())
            val gson = Gson()
            val configurationData = gson.fromJson<GameData>(mJson, GameData::class.java)
            Log.e("", "" + configurationData)

            configurationData.configurations.forEach {
                if(it.eventid == Preferences.eventid)
                {
                    Preferences.selectedGame=it;
                }
            }

            if(Preferences.selectedGame.eventid.isNullOrBlank())
            {
                Preferences.selectedGame=configurationData.configurations[0];
            }

            Log.e("", "" + obj)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        backBtn.click { finishAlpha() }
        fetchEvents()
        addEventsView()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token.toString()

                getSendDeviceToken(token)

            })

    }

    private fun addEventsView() {

        if(Preferences.eventResponse.isEmpty()){
            return
        }

        val selectedData = Preferences.eventResponse.single {
            it.location == Preferences.locationId
        }

        // val imgUrl = "https://api-dev.festfeed.com/event/".plus(selectedData.id).plus("/cover")
        val imgUrl = "https://api-glow.festfeed.com/event/".plus(selectedData.id).plus("/cover")

        Glide.with(this).load(imgUrl).placeholder(R.drawable.christmas_back_splash).into(topBg)

        val sortedList=  selectedData.buttons.sortedBy { it.position }
        selectedData.buttons.clear()
        selectedData.buttons.addAll(sortedList)


        eventsButtons.removeAllViews()
        if (Paper.book().contains("events")) {
            if (Preferences.eventResponse.isEmpty()) {
                return;
            }

            var counts = selectedData.buttons.size / 3

            if (selectedData.buttons.size % 3 != 0) {
                counts += 1
            }

            var pos = 0

            for (i in 0 until counts) {

                if (pos == selectedData.buttons.size) {
                    return
                }
                val parent = LinearLayout(this)

                parent.apply {
                    layoutParams = LinearLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT, 1.0f
                    ).apply {
                        orientation = LinearLayout.HORIZONTAL
                    }



                    for (j in 0..2) {

                        val layoutParams = LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
                        ).apply {
                            setMargins(0, 0, 0, 50)
                        }


                        val buttonView = ButtonView(
                            this@DashboardActivity,
                            if (pos >= selectedData.buttons.size) Button() else
                                selectedData.buttons[pos]
                        ) {

                            when (it.name.toLowerCase(Locale.getDefault())) {
                                "info" -> startActivityWithUp(intentTo(InfoActivity::class.java))
                                "schedule" -> startActivityWithUp(intentTo(ScheduleActivity::class.java))
                                "map" -> startActivityWithUp(intentTo(MapsActivity::class.java))
                                "tickets" -> startActivityWithUp(
                                    intentTo(WebViewActivity::class.java).putExtra(
                                        "url",
                                        it.url
                                    )
                                )
                                "game" ->{
                                    if((pref.getInt(Preferences.selectedGame.eventid + "count", 0) >0))
                                    {
                                        startActivityWithUp(intentTo(EventQuest::class.java))
                                    }else
                                    {
                                        startActivityWithUp(intentTo(GameInitActivity::class.java))
                                    }
                                }
                                "vendor" -> startActivityWithUp(
                                    intentTo(WebViewActivity::class.java).putExtra(
                                        "url",
                                        it.url
                                    )
                                )
                                "faq" -> startActivityWithUp(
                                    intentTo(WebViewActivity::class.java).putExtra(
                                        "url",
                                        it.url
                                    )
                                )
                                "scann" -> startActivityWithUp(
                                    intentTo(WebViewActivity::class.java).putExtra(
                                        "url",
                                        it.url
                                    )
                                )
                            }

                        }
                        buttonView.layoutParams = layoutParams
//
                        buttonView.setBackgroundColor(0x00000000)
                        addView(buttonView)

                        pos++
                    }
                    eventsButtons.addView(parent)
                }


            }
        }


    }

    override fun onBackPressed() {
        finishMain()
    }


    private fun fetchEvents() {
        RetrofitClient.instance.getEvents(
            Preferences.auth
        ).enqueue(object : Callback<List<EventResponse>> {
            override fun onFailure(call: Call<List<EventResponse>>, t: Throwable) {
                Log.e("", "" + t)
            }

            override fun onResponse(
                call: Call<List<EventResponse>>,
                response: Response<List<EventResponse>>
            ) {

                if (response.code() == 200) {
                    try {
                        Preferences.eventResponse = response.body()!!
                        addEventsView()
                    } catch (e: Exception) {
                    }
                }
            }

        })
    }
    fun readJSONFromAsset(): String? {
        var json: String? = null
        try {
            val filedata = assets.open("GameConfiguration.json")
            val size = filedata.available()
            val buffer = ByteArray(size)
            filedata.read(buffer)
            filedata.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }


    private fun getSendDeviceToken(token: String) {

        RetrofitClient.instance.registerToken(Preferences.auth,JSONObject().put("token", token).toString())
            .enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<JsonElement>,
                    response: Response<JsonElement>
                ) {
                    Log.e("",""+response);
                    if (response.code() == 200) {


                    } else if (response.code() == 422) {

                    } else {

                    }

                }
            })

    }
}
