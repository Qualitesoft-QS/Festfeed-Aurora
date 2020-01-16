package com.festfeed.awf.customviews

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.festfeed.awf.R
import com.festfeed.awf.models.Button
import com.festfeed.awf.utils.click
import java.util.*




class ButtonView(context: Context, private val button: Button, callback :(button : Button)-> Unit) : LinearLayoutCompat(context) {

    init {
        orientation = VERTICAL;
        gravity = Gravity.CENTER_VERTICAL;
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_view_button, this, true)
        val mImage = view.findViewById<AppCompatImageView>(R.id.imageView)





        try {
            val stringToFilter = button.image
            var stringWithOnlyDigits = stringToFilter.filter { it.isLetterOrDigit() }
            stringWithOnlyDigits=  stringWithOnlyDigits.replace("-","")
            val res = resources.getIdentifier(stringWithOnlyDigits.toLowerCase(), "drawable", context.getPackageName())

            mImage.setImageResource(res)
        } catch (e: Exception) {
        }
//        when(button.name.toLowerCase(Locale.getDefault())){
//            "eventinfo" ->   mImage.setImageResource(R.drawable.eventinfo)
//            "eventschedule" -> mImage.setImageResource(R.drawable.eventschedule)
//            "eventmap" -> mImage.setImageResource(R.drawable.eventmap)
//            "eventtickets" -> mImage.setImageResource(R.drawable.eventtickets)
//            "eventgame" -> mImage.setImageResource(R.drawable.eventgame)
//            "vendor" -> mImage.setImageResource(R.drawable.eventvendors)
//            "eventfaq" -> mImage.setImageResource(R.drawable.eventfaq)
//            "scann" -> mImage.setImageResource(R.drawable.eventgame)
//        }
        val title = view.findViewById<AppCompatTextView>(R.id.textView)
        title.text = button.name

        click { callback(button) }


    }

}