package com.gates.uniongates

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.frame3.*
import kotlinx.android.synthetic.main.frame3.p3frame2
import kotlinx.android.synthetic.main.frame3.p3frame3
import kotlinx.android.synthetic.main.frame_adapter.*
import kotlinx.android.synthetic.main.panel_03_02.*
import kotlinx.android.synthetic.main.panel_2_01.*

class Fragment3of1:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frame3,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        p3frame1.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",1)
            intent.putExtra("size",500)
            startActivityForResult(intent,20)
        }
       p3frame2.setOnClickListener {
           val intent= Intent(context,PanelGallary::class.java)
           intent.putExtra("imageview",2)
           intent.putExtra("size",500)
           startActivityForResult(intent,20)
       }
        p3frame3.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",3)
            intent.putExtra("size",500)
            startActivityForResult(intent,20)
        }
    }
    fun updateImage(urlImg:String,selImg:Int){
        when(selImg){
            1->{
                context?.let { Glide.with(it).load(urlImg).into(p3frame1) }
            }
            2->{
                context?.let { Glide.with(it).load(urlImg).into(p3frame2) }
            }
            3->{
                context?.let { Glide.with(it).load(urlImg).into(p3frame3) }
            }
        }
    }
    fun numPanels():Int{
        return 3
    }
}