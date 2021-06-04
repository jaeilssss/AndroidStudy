package com.example.remoteconfigproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CoroutineScope(Dispatchers.Main).launch {
            var list =  initData()
            var viewPager = findViewById<ViewPager2>(R.id.viewpager)
            var adapter = viewPagerAdapter(list,applicationContext)
            viewPager.adapter = adapter

        }
    }

suspend  fun initData() : ArrayList<String>{
    var list = ArrayList<String>()
    var remoteConfig = Firebase.remoteConfig

    remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
    )
//    remoteConfig.setDefaultsAsync()
    remoteConfig.fetchAndActivate().addOnCompleteListener {
        if(it.isSuccessful){
            list = parsingJson(remoteConfig.getString("main"))
        }
    }.addOnFailureListener {

    }.await()
    return list
}

     fun parsingJson(json : String) :ArrayList<String>{
        val jsonArray = JSONArray(json)
         var list = ArrayList<String>()
        for(index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let{
                list.add(it.getString("path"))
            }
        }
            return list;
    }
}