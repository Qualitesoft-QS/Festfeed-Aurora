package com.festfeed.awf.utils

import com.festfeed.awf.gamemodel.Configuration
import com.festfeed.awf.models.EventResponse
import com.festfeed.awf.models.LoginResponse
import io.paperdb.Paper

object Preferences {


    var auth: String
        get() = Paper.book().read("auth", "")
        set(value) {
            Paper.book().write("auth", value)
        }

    var loginResponse: LoginResponse
        get() = Paper.book().read<LoginResponse>("login")
        set(value) {
            Paper.book().write("login", value)
        }

    var eventResponse: List<EventResponse>
        get() = Paper.book().read<List<EventResponse>>("events", arrayListOf())
        set(value) {
            Paper.book().write("events", value)
        }


    var favEventList: ArrayList<String>
        get() = Paper.book().read("favEventList", arrayListOf())
        set(value) {
            Paper.book().write("favEventList", value)
        }

    var events: List<EventResponse>
        get() = Paper.book().read<List<EventResponse>>("eventsList")
        set(value) {
            Paper.book().write("eventsList", value)
        }


    var isLoggedIn: Boolean
        get() = Paper.book().read("isLoggedIn", false)
        set(value) {
            Paper.book().write("isLoggedIn", value)
        }

    var locationId: String
        get() = Paper.book().read("selectedLocation", "")
        set(value) {
            Paper.book().write("selectedLocation", value)
        }

    var eventid: String
        get() = Paper.book().read("eventid", "")
        set(value) {
            Paper.book().write("eventid", value)
        }



    var selectedGame: Configuration
        get() = Paper.book().read("selectedGame", Configuration())
        set(value) {
            Paper.book().write("selectedGame", value)
        }


}