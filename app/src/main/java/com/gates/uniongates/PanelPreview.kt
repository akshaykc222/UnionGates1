package com.gates.uniongates

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gates.uniongates.objects.Panel
import com.gates.uniongates.objects.PanelGallary
import kotlinx.android.synthetic.main.activity_panel_preview.*

class PanelPreview : AppCompatActivity() {
    val CODE=12
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_panel_preview)
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)
        val bundle=intent.extras
        val SEL_PANEL=bundle?.getInt("SEL_PANEL")
        val obj: Panel = bundle?.getSerializable("panelpro") as Panel
        Glide.with(this).load(obj.panel_img).into(sel_panel)
        sel_price.text= obj.panel_cost
        description.text=obj.panel_type+"\n"+"Width :\t"+obj.panelWidth+"\n"+"Height :\t"+obj.panelHeight+"\n"+obj.panel_desc2+"\n"
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        add_panel.setOnClickListener {
            val intent=Intent()
            intent.putExtra("SEL_PANEL",SEL_PANEL)
            intent.putExtra("PANEL_PROPERTIES",obj)
            setResult(20,intent)
            finish()
        }
    }

    override fun onBackPressed() {
   /*  val sessionManager=SessionManager(this)
      val obj=sessionManager.getCurentSel()
      val intent=Intent(this,com.gates.uniongates.PanelGallary::class.java)
      intent.putExtra("imageview",obj.ImageView)
      intent.putExtra("size",obj.panelSize)
        startActivity(intent)
        finish()*/
        super.onBackPressed()
    }
}