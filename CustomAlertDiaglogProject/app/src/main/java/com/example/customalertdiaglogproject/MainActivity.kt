package com.example.customalertdiaglogproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.btn).setOnClickListener {
            CustomDialog(this)
                .setTitle("제목")
                .setMessage("내용")
                .setPositiveButton("예") {
                    Toast.makeText(this, "예", Toast.LENGTH_SHORT).show()
                }.setNegativeButton("아니오") {
                    Toast.makeText(this, "아니오", Toast.LENGTH_SHORT).show()
                }.show()
        }

    }
}