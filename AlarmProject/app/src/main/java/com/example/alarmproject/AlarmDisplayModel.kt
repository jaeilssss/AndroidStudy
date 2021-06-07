package com.example.alarmproject

data class AlarmDisplayModel(
        val hour : Int,
        val minute : Int,
        var onOff : Boolean
){
    val timeText : String
        get() {
            val h = "%02d".format(if(hour<12) hour else hour -12)
            // %02d 는 스트링 포멧으로 2자리까지 인트형이 들어올수 있고 공백은 0으로 채운다 라는 의미!

            val m = "%02d".format(minute)

            return "$h:$m"
        }
    val amPmText : String
        get() {
            return if(hour <12) "AM" else "PM"
        }


}