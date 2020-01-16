package com.festfeed.awf.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.festfeed.awf.R
import com.festfeed.awf.activities.gamescan.EventQuest
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.gamemodel.GameData
import com.festfeed.awf.utils.*
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_game_init.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class GameInitActivity : BaseActivity() {

    override fun layoutResID() = R.layout.activity_game_init

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

        val stringToFilter = Preferences.selectedGame.initialScreenData.image
        var stringWithOnlyDigits = stringToFilter.filter { it.isLetterOrDigit() }
        stringWithOnlyDigits=  stringWithOnlyDigits.replace("-","")
        var drawable=  resources.getDrawable(resources.getIdentifier(stringWithOnlyDigits, "drawable", packageName));
        splashLogo.setBackgroundDrawable(drawable);

        gameInitDetailText.setText(Preferences.selectedGame.initialScreenData.text)

        backBtn.click { onBackPressed() }
        beginQuestBtn.click { startActivitySideRight(intentTo(EventQuest::class.java),true) }
    }

    override fun onBackPressed() {
        finishDown()
    }



}
