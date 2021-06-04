package com.example.viewpager2project

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var list = ArrayList<Int>()

        list.add(Color.parseColor("#ffff00"))
        list.add(Color.parseColor("#bdbdbd"))
        list.add(Color.parseColor("#0f9231"))
        var adater = ViewPager2Adater(list,applicationContext)

        var viewpager = findViewById<ViewPager2>(R.id.viewpager2)
        viewpager.offscreenPageLimit=3
        viewpager.getChildAt(0).overScrollMode=View.OVER_SCROLL_NEVER
        viewpager.adapter = adater

        var transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))

        transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
            var v = 1-Math.abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        })

        viewpager.setPageTransformer(transform)

        var tab = findViewById<TabLayout>(R.id.tab)

        TabLayoutMediator(tab,viewpager){_,_ ->  }.attach()
    }
}