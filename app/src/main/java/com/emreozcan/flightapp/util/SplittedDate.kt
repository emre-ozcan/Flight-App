package com.emreozcan.flightapp.util

data class SplittedDate(
    val day: Int,
    val month: Int,
    val year: Int
)

fun dateSplitter(date: String): SplittedDate {

    val yearAndMonthDay = date.split(",")
    val yearString = yearAndMonthDay[1].trim()
    val year = yearString.toInt()

    val monthAndDay = yearAndMonthDay[0].split(" ")
    val day = monthAndDay[1].toInt()
    val month = intMont(monthAndDay[0])

    return SplittedDate(day,month,year)

}

fun intMont(stringMonth: String): Int{
    return when(stringMonth){
        "Jan" -> 0
        "Feb" -> 1
        "Mar" -> 2
        "Apr" -> 3
        "May" -> 4
        "Jun" -> 5
        "Jul" -> 6
        "Aug" -> 7
        "Sep" -> 8
        "Oct" -> 9
        "Nov" -> 10
        "Dec" ->11
        else ->
            12
    }
}