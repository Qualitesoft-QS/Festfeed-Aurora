package com.festfeed.awf.models

data class EventResponse(
    val admins: List<String>,
    var buttons: ArrayList<Button>,
    val color: String,
    val created: Long,
    val description: String,
    val end_time: Long,
    val id: String,
    val location: String,
    val locations: List<Location>,
    val name: String,
    val published: Boolean,
    val startTime: String,
    var schedule_items: ArrayList<ScheduleItem>,
    val start_time: Long

)