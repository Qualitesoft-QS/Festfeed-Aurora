package com.festfeed.awf.utils

import android.content.Intent
import android.content.res.Resources
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.festfeed.awf.activities.SelectCityActivity
import spencerstudios.com.bungeelib.Bungee
import spencerstudios.com.bungeelib.R
import java.text.SimpleDateFormat
import java.util.*


fun runWithDelay(millis: Long = 3000, executor: () -> Unit) {
    Handler().postDelayed({
        executor()
    }, millis)
}

fun AppCompatActivity.startActivityWithAlpha(intent: Intent, finish: Boolean = false) {
    startActivity(intent)
    Bungee.fade(this)
    if (finish) finish()
}

fun AppCompatActivity.startActivityWithUp(intent: Intent, finish: Boolean = false) {
    startActivity(intent)
    Bungee.slideUp(this)
    if (finish) finish()
}

fun AppCompatActivity.startActivitySideRight(intent: Intent, finish: Boolean = false) {
    startActivity(intent)
    Bungee.slideLeft(this)
    if (finish) finish()
}

fun AppCompatActivity.intentTo(cls: Class<*>) = Intent(this, cls)

fun AppCompatActivity.finishAlpha(){
    // finish()
    startActivityWithAlpha(intentTo(SelectCityActivity::class.java), true)
    Bungee.fade(this)
}

fun AppCompatActivity.finishMain(){
    finish()
    // startActivityWithAlpha(intentTo(SelectCityActivity::class.java), true)
    Bungee.fade(this)
}


fun AppCompatActivity.finishDown(){
    finish()
    overridePendingTransition(0, R.anim.slide_down_exit)
}

fun AppCompatActivity.finishLeft(){
    finish()
    Bungee.slideRight(this)
}

fun View.click(onClick: ()->Unit){
    setOnClickListener { onClick() }
}

fun AppCompatImageView.setCircleImage(url : String){
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}
fun View.parseDate(time: Long, format: String) : String{
    val formatter = SimpleDateFormat(format)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time *1000
    return formatter.format(calendar.time)

}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()