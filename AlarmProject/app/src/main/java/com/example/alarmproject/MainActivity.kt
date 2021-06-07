package com.example.alarmproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // step0 뷰를 초기화해주기
        initOnOffButton()
        initChangeAlarmTimeButton()

        //step1 데이터 가저오기

        //step2 뷰에 데이터 그려주기


    }

    private fun initOnOffButton(){
        val onOffButton = findViewById<Button>(R.id.alarmOn)
        onOffButton.setOnClickListener {
            // 데이터를 확인한다 .

            // 온오프 에 따라 작업을 처리한다

            // 오프 -> 알람을 제거

            //온 -> 알람을 등록

            //데이터를 저장한다 .
        }
    }
    private fun initChangeAlarmTimeButton(){
        val changeButton = findViewById<Button>(R.id.changeAlarmButton)
        changeButton.setOnClickListener {
            //현재 시간을 일단 가저온다

            // TimePickDialog 띄어줘서 시간을 성정을 하도록 하게끔 하고 그 시간을 가저와서

            // 데이터를 저장한다.

            // 뷰를 업데이트 한다
            // 기존에 있던 알람을 삭제한다

        }
    }
}