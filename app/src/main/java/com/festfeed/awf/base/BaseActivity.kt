package com.festfeed.awf.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.festfeed.awf.models.EventResponse
import com.festfeed.awf.network.RetrofitClient
import com.festfeed.awf.utils.Preferences
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseActivity : AppCompatActivity() {

    abstract fun layoutResID(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResID())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(base))
    }



    override fun onResume() {
        super.onResume()

        fetchEvents()
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
                        //addEventsView()
                    } catch (e: Exception) {
                    }
                }
            }

        })
    }
}