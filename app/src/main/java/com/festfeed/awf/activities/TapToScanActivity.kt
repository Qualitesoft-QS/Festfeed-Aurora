package com.festfeed.awf.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.festfeed.awf.R
import com.festfeed.awf.base.BaseActivity
import com.festfeed.awf.utils.click
import com.festfeed.awf.utils.finishLeft
import com.festfeed.awf.utils.intentTo
import com.festfeed.awf.utils.startActivitySideRight
import kotlinx.android.synthetic.main.activity_tap_to_scan.*


class TapToScanActivity : BaseActivity() {

    override fun layoutResID() = R.layout.activity_tap_to_scan

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
        needInstructionBtn.click { startActivitySideRight(intentTo(InstructionsActivity::class.java)) }
    }

    override fun onBackPressed() {
        finishLeft()
    }
}
