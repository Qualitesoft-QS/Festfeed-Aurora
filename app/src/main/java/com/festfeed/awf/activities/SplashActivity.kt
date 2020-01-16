package com.festfeed.awf.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.festfeed.awf.R
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.models.LoginResponse
import com.festfeed.awf.network.RetrofitClient
import com.festfeed.awf.utils.Preferences
import com.festfeed.awf.utils.intentTo
import com.festfeed.awf.utils.startActivityWithAlpha
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : BaseActivity() {

    override fun layoutResID() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
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
        // FirebaseMessaging.getInstance().isAutoInitEnabled = true
        progressBar.animate()
            .alpha(1f)
            .setDuration(1000)
            .setStartDelay(2000)
            .withStartAction {
                fetchCities()
            }
            .start()

    }

    @SuppressLint("HardwareIds")
    private fun fetchCities() {

        var androidId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )

            //androidId ="eeeee"
            getAuthorizationToken(androidId)




    }
//    f1GVbKwdPRo:APA91bFmuyCyw2ArsXVSHYBaV3u2njrDTzWRf3rip-U-SD8IY_NTN7po3S8HiIysJo1wrv3-TPV6jYzY6LslZ_8gZ3KIOyp05PXP9fTqKUi_jdPaw-P78aRUaSuJnx2sL9bUHcwHyKCO
//{"created":"2019-10-28T16:43:59.1572281039Z","id":"009bcc08-f088-4732-9bba-d3d7f59ece29"}

    private fun getAuthorizationToken(token: String) {
        RetrofitClient.instance.createUser(JSONObject().put("device_id", token).toString())
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.code() == 200) {

                        val headerList = response.headers()
                        Preferences.auth = headerList["X-Api-Key"] ?: ""
                        Preferences.loginResponse = response.body()!!
                        startActivityWithAlpha(intentTo(SelectCityActivity::class.java), true)
                    } else if (response.code() == 422) {
                        performLogin(token)
                    } else {
                        val errorBody = JSONObject(response.errorBody()?.string()!!)
                        Toast.makeText(
                            applicationContext,
                            errorBody.getJSONObject("errors").getJSONArray("device_id").getString(0),
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
            })

    }





    private fun performLogin(token: String) {
        RetrofitClient.instance.loginUser(JSONObject().put("device_id", token).toString())
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.code() == 200) {

                        val headerList = response.headers()
                        Preferences.auth = headerList["X-Api-Key"] ?: ""
                        Preferences.loginResponse = response.body()!!

                        if (!Preferences.isLoggedIn) {
                            startActivityWithAlpha(intentTo(SelectCityActivity::class.java), true)
                        } else {
                            startActivityWithAlpha(intentTo(DashboardActivity::class.java), true)
                        }
                    } else {
                        val errorBody = JSONObject(response.errorBody()?.string()!!)
                        Toast.makeText(
                            applicationContext,
                            errorBody.getJSONObject("errors").getJSONArray("device_id").getString(0),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })
    }

}
