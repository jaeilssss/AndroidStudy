package com.example.remoteconfigproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()

    }

private fun initData(){
    var remoteConfig = Firebase.remoteConfig

    remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
    )
//    remoteConfig.setDefaultsAsync()

    remoteConfig.fetchAndActivate().addOnCompleteListener(this) {
        if(it.isSuccessful){
             parsingJson(remoteConfig.getString("main"))
        }
    }.addOnFailureListener {

    }
}

    fun parsingJson(json : String) {
        val jsonArray = JSONArray(json)

        for(index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let{
                list.add(it.getString("path"))
            }
        }
        for(i in 0 until list.size){
            println(list.get(i))
        }
    }
}