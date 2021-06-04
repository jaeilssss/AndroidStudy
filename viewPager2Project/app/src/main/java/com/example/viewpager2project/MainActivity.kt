package com.example.viewpager2project

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    var layoutOnBoardingIndicators : LinearLayout ?=null
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


        layoutOnBoardingIndicators = findViewById(R.id.indicators)
        setupOnBoardingIndicators()
        setCurrentOnboardingIndicator(0)

                var transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))

        transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
            var v = 1-Math.abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        })

        viewpager.setPageTransformer(transform)


        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position : Int){
                setCurrentOnboardingIndicator(position)
            }

        })
    }

    private fun setupOnBoardingIndicators(){
        val indicators =
            arrayOfNulls<ImageView>(3)

        var layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(8,0,8,0)

        for( i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(
                applicationContext,
                R.drawable.onboarding_indicator_inactivie
            ))

            indicators[i]?.layoutParams = layoutParams

            layoutOnBoardingIndicators?.addView(indicators[i])
        }
    }

    private fun setCurrentOnboardingIndicator( index : Int){
        var childCount = layoutOnBoardingIndicators?.childCount
        for(i in  0 until childCount!!){
            var imageView = layoutOnBoardingIndicators?.getChildAt(i) as ImageView
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.onboarding_indicator_active))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.onboarding_indicator_inactivie))
            }
        }
    }
}