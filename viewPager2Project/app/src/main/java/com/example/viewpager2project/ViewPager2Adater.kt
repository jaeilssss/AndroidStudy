package com.example.viewpager2project

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ViewPager2Adater(var list : ArrayList<Int>,var context : Context)  : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return viewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        Glide.with(context).load(list.get(position)).into((holder as viewHolder).image)
        (holder as viewHolder).image.setBackgroundColor(list.get(position))
    }

    inner class viewHolder(var view : View) : RecyclerView.ViewHolder(view){
            var image : ImageView  = view.findViewById(R.id.image)
    }
}