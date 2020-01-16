package com.festfeed.awf.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.festfeed.awf.R
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.utils.*
import kotlinx.android.synthetic.main.activity_instructions.*


class InstructionsActivity : BaseActivity() {

    override fun layoutResID() = R.layout.activity_instructions

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
        titlegame.setText(Preferences.selectedGame.instructionsScreenData.title)
        gameInitDetailText.setText(Preferences.selectedGame.instructionsScreenData.text)
        backBtn.click { onBackPressed() }
    }

    override fun onBackPressed() {
        finishLeft()
    }
}
