package com.gates.uniongates

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.frame3.*

class FragmentSelectedFrame:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame3)
        p3frame1.setOnClickListener {
            val intent=Intent(this,PanelGallary::class.java)
            intent.putExtra("imageview",1)
            startActivityForResult(intent,20)
        }
        p3frame2.setOnClickListener {
            val intent=Intent(this,PanelGallary::class.java)
            intent.putExtra("imageview",2)
            startActivityForResult(intent,20)
        }
        p3frame3.setOnClickListener {
            val intent=Intent(this,PanelGallary::class.java)
            intent.putExtra("imageview",3)
            startActivityForResult(intent,20)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==20){
            val sel_img=data?.getIntExtra("SEL_PANEL",0)
            val gh=data?.getSerializableExtra("PANEL_PROPERTIES")  as com.gates.uniongates.objects.PanelGallary
            when(sel_img){
                0 -> Toast.makeText(this,"please try again",Toast.LENGTH_LONG).show()
                1 -> { Glide.with(this).load(gh.image).into(p3frame1)}
                2 -> { Glide.with(this).load(gh.image).into(p3frame2)}
                3 -> { Glide.with(this).load(gh.image).into(p3frame3)}
            }

            Toast.makeText(this,"result getted"+gh.price,Toast.LENGTH_LONG).show()
        }
    }
}