package com.gates.uniongates.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.gates.uniongates.R
import com.google.android.material.slider.Slider
import java.util.*
import kotlin.collections.ArrayList

class IntroSlider(val mctx:Context,val SliderImages:ArrayList<Int>):PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ==`object` as ConstraintLayout
    }

    override fun getCount(): Int {
        return SliderImages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val h=SliderImages[position]
        val itemView=LayoutInflater.from(mctx).inflate(R.layout.image_slider,container,false)
        val imageView:ImageView=itemView.findViewById(R.id.introImg)
        imageView.setImageResource(h)
        val container_vedio=itemView.findViewById<LinearLayout>(R.id.vedio_btn)
        if (position==1 ||position==2){
            container_vedio.visibility=View.VISIBLE
        }else{
            container_vedio.visibility=View.GONE
        }

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
       container.removeView(`object` as ConstraintLayout)
    }
}