package com.festfeed.awf.activities

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import androidx.core.text.HtmlCompat
import com.festfeed.awf.R
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.utils.*
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : BaseActivity() {

    private val format = "MMM dd, YYYY @ hh:mm a"

    override fun layoutResID() = R.layout.activity_info

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
        backBtn.click { onBackPressed() }

        val selectedData = Preferences.eventResponse.single {
            it.location == Preferences.locationId
        }

        infoDp.setCircleImage("http://cdn.jobmonkey.com/wp-content/uploads/2015/03/concertsm.jpg")
        startText.text =
            startText.parseDate(selectedData.start_time, format)
        endText.text = endText.parseDate(selectedData.end_time, format)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            descText.text = Html.fromHtml(selectedData.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
//        }else{
//            descText.text = Html.fromHtml(selectedData.description)
//        }
        descText.text =selectedData.description
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishDown()
    }
}