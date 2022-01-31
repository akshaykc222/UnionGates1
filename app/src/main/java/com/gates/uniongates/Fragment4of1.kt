package com.gates.uniongates

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.panel_03_02.*
import kotlinx.android.synthetic.main.panel_04_01.*

class Fragment4of1:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.panel_04_01,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        p4_1frame1.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",1)
            intent.putExtra("size",500)
            startActivityForResult(intent,20)
        }
        p4_1frame2.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",2)
            intent.putExtra("size",500)
            startActivityForResult(intent,20)
        }
        p4_1frame3.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",3)
            intent.putExtra("size",500)
            startActivityForResult(intent,20)
        }
        p4_1frame4.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",4)
            intent.putExtra("size",500)
            startActivityForResult(intent,20)
        }
    }
    fun updateImage(urlImg:String,selImg:Int){
        when(selImg){
            1->{
                context?.let { Glide.with(it).load(urlImg).into(p4_1frame1) }
            }
            2->{
                context?.let { Glide.with(it).load(urlImg).into(p4_1frame2) }
            }
            3->{
                context?.let { Glide.with(it).load(urlImg).into(p4_1frame3) }
            }
            4->{
                context?.let { Glide.with(it).load(urlImg).into(p4_1frame4) }
            }
        }
    }
    fun numPanels():Int{
        return 4
    }
}