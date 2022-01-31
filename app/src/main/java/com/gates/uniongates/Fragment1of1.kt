package com.gates.uniongates

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.panel_01.*

class Fragment1of1:Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.panel_01,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        p1frame1.setOnClickListener {
            val intent= Intent(context,PanelGallary::class.java)
            intent.putExtra("imageview",1)
            intent.putExtra("size",1200)
            startActivityForResult(intent,20)
        }
    }

    fun updateImage(urlimg:String){
        Log.i("akshay","wrked")
        context?.let { Glide.with(it).load(urlimg).into(p1frame1) }
    }
    fun numPanels():Int{
        return 1
    }


}