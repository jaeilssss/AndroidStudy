package com.example.alarmproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initOnOffButton()
        initChangeAlarmTimeButton()

        //step1 데이터 가저오기

        val model = fetchDataFromSharedPreferences()
        //step2 뷰에 데이터 그려주기
        renderView(model)

    }

    private fun initOnOffButton(){
        val onOffButton = findViewById<Button>(R.id.alarmOn)
        onOffButton.setOnClickListener {

            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = saveAlarmModel(model.hour,model.minute,model.onOff.not())
            renderView(newModel)
            if(newModel.onOff){
                // 켜진경우 -> 알람을 등록
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY,newModel.hour)
                    set(Calendar.MINUTE,newModel.minute)
                    if(before(Calendar.getInstance())){
                        add(Calendar.DATE,1)
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                )
            }else{
                // 알람꺼진경우 -> 알람제거
                cancleAlarm()
            }

        }
    }
    private fun initChangeAlarmTimeButton(){
        val changeButton = findViewById<Button>(R.id.changeAlarmButton)
        changeButton.setOnClickListener {

            val calendar = Calendar.getInstance()

            TimePickerDialog(this,{picker,hour,minute->

                // 데이터를 저장한다.
                val model = saveAlarmModel(hour,minute,false)
                // 뷰를 업데이트 한다
                renderView(model)
                cancleAlarm()
            }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show()

        }
    }

    private fun saveAlarmModel(
            hour : Int,
            minute : Int,
            onOff : Boolean
            ): AlarmDisplayModel {
        val model = AlarmDisplayModel(
                hour = hour,
                minute = minute,
                onOff = onOff
        )

            val sharedPreferences = getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)

            with(sharedPreferences.edit()) {
                putString(ALARM_KEY, model.makeDataForDB())
                putBoolean(ONOFF_KEY, model.onOff)
                commit()
            }
            return model

    }
    private fun fetchDataFromSharedPreferences() : AlarmDisplayModel{
        val sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE)
        val timeDBValue = sharedPreferences.getString(ALARM_KEY,"9:30") ?:"9:30"

        val onOffDBValue=sharedPreferences.getBoolean(ONOFF_KEY,false)

        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
                hour = alarmData[0].toInt(),
                minute = alarmData[1].toInt(),
                onOff = onOffDBValue
        )

        // 보정 예외처리

        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,Intent(this,
                AlarmReceiver::class.java),PendingIntent.FLAG_NO_CREATE)

        if((pendingIntent==null)and alarmModel.onOff){
            //알람은 꺼저있는대 데이터는 켜저있는 경우

            alarmModel.onOff = false

        }else if((pendingIntent!=null) and alarmModel.onOff.not()){
            // 알람은 켜저있는대 , 데이터는 꺼져있는 경우
            // 알람은 취소함
            pendingIntent.cancel()
        }
        return alarmModel
    }

    private fun renderView(model : AlarmDisplayModel){
        findViewById<TextView>(R.id.ampmTextView).apply{
            text = model.amPmText
        }
        findViewById<TextView>(R.id.timeTextView).apply{
            text = model.timeText
        }
        findViewById<Button>(R.id.alarmOn).apply {
            text = model.onOffText
            tag = model
        }
    }

    private fun cancleAlarm(){
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,Intent(this,
                AlarmReceiver::class.java),PendingIntent.FLAG_NO_CREATE)
        pendingIntent?.cancel()
    }
    companion object {
        private const val SHARE_PREFERENCE_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1000
    }
}