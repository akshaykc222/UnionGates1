package com.gates.uniongates.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gates.uniongates.PanelPreview
import com.gates.uniongates.R
import com.gates.uniongates.SessionManager
import com.gates.uniongates.objects.Panel
import com.gates.uniongates.objects.PanelGallary

class PanelGallaryAdapter(val mctx:Context,val GallaryItem:ArrayList<Panel>,val imageviewNum:Int):RecyclerView.Adapter<PanelGallaryAdapter.ViewHolder>(){
   private val activity:com.gates.uniongates.PanelGallary= mctx as com.gates.uniongates.PanelGallary
    class ViewHolder(item: View):RecyclerView.ViewHolder(item){
        val image:ImageView=item.findViewById(R.id.panel_img)
        val price:TextView=item.findViewById(R.id.panel_price)
        val card:CardView=item.findViewById(R.id.cardpanel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item=LayoutInflater.from(mctx).inflate(R.layout.panel_adapter,parent,false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return GallaryItem.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val h=GallaryItem[position]
        val pref=SessionManager(mctx);

        if (h.isFree!=null){
            if (!h.isFree!! && pref.GetPhone() == "1"){
                holder.itemView.visibility=View.GONE
            }
        }
        var Res:Drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Res= ContextCompat.getDrawable(mctx,R.drawable.app_logo)!!
        }else{
            Res= mctx.resources.getDrawable(R.drawable.app_logo)
        }
        Log.i("adapter", h.panel_img.toString())
        Glide.with(mctx).load(h.panel_img)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(holder.image)

                .onLoadFailed(Res)
        holder.price.text="₹\t"+h.panel_cost
        holder.card.setOnClickListener {
            val intenet=Intent(mctx,PanelPreview::class.java)
          //  val intenet=Intent()
            val obj:Panel=h
            intenet.putExtra("panelpro",obj)
            intenet.putExtra("SEL_PANEL",imageviewNum)
            intenet.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            mctx.startActivity(intenet)
          //  activity.setResult(20,intenet)
            activity.finish()
        }
    }


}