package com.emreozcan.flightapp.util

class TimeDifference(
    internal var hours: Int,
    internal var minutes: Int,
    internal var seconds: Int = 0
)

fun difference(start: TimeDifference, stop: TimeDifference): TimeDifference {
    val diff = TimeDifference(0, 0,0 )

    if (stop.seconds > start.seconds) {
        --start.minutes
        start.seconds += 60
    }

    diff.seconds = start.seconds - stop.seconds
    if (stop.minutes > start.minutes) {
        --start.hours
        start.minutes += 60
    }

    diff.minutes = start.minutes - stop.minutes
    diff.hours = start.hours - stop.hours

    return diff
}
