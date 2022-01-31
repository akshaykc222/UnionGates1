package com.gates.uniongates

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.panel_02_02.*
import kotlinx.android.synthetic.main.panel_2_01.*

class Fragment2of2:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.panel_02_02,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        p2_2frame1.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",1)
            intent.putExtra("size",1200)
            startActivityForResult(intent,20)
        }
        p2_2frame2.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",2)
            intent.putExtra("size",500)
            startActivityForResult(intent,20)
        }
    }
    fun updateImage(urlImg:String,selImg:Int){
        try {
            Log.i("akshay",urlImg+"\t"+selImg)
            when(selImg){
                1->{
                    context?.let { Glide.with(it).load(urlImg).into(p2_2frame1) }
                }
                2->{
                    context?.let { Glide.with(it).load(urlImg).into(p2_2frame2) }
                }
            }
        }catch (E:Exception){
            Log.i("akshay",E.toString())
        }

    }
    fun numPanels():Int{
        return 2
    }
}