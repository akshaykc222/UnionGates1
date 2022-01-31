package com.gates.uniongates.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gates.uniongates.R
import com.gates.uniongates.SelectedFrameActivity
import com.gates.uniongates.objects.Frames

class FrameGallary(val mctx:Context,val GallryList:ArrayList<Frames>):RecyclerView.Adapter<FrameGallary.ViewHolder>() {
    class ViewHolder(item: View):RecyclerView.ViewHolder(item) {
        val image:ImageView=item.findViewById(R.id.frame_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item=LayoutInflater.from(mctx).inflate(R.layout.frame_gallary_adapter,parent,false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return GallryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val h=GallryList[position]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Glide.with(mctx).load(h.frame_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(holder.image)
                    .onLoadStarted(mctx.getDrawable(R.drawable.app_logo))

        }else{
            Glide.with(mctx).load(h.frame_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image)
                    .onLoadStarted(AppCompatResources.getDrawable(mctx,R.drawable.app_logo))
        }
//        holder.inner_element.text=h.frameInner
//        holder.outter_element.text=h.frameOuter
       // holder.frame_code.text=h.frame_code
        holder.itemView.setOnClickListener {
            val intent=Intent(mctx,SelectedFrameActivity::class.java)
            intent.putExtra("proprties",h)
            mctx.startActivity(intent)
        }
    }
}