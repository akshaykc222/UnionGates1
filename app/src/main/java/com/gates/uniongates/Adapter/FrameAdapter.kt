package com.gates.uniongates.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview. widget.RecyclerView
import com.gates.uniongates.FragmentSelectedFrame
import com.gates.uniongates.R
import com.gates.uniongates.objects.FrameType

class FrameAdapter(val mctx:Context,val itemList:ArrayList<FrameType>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val FRAME1 = 1
        const val FRAME2 = 2
        const val FRAME3 = 3
    }
  inner  class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(model:FrameType){
            val frame1:ImageView =itemView.findViewById(R.id.frame1)
            val frame2:ImageView =itemView.findViewById(R.id.p3frame2)
            val frame3:ImageView =itemView.findViewById(R.id.p3frame3)
            val gate_width:TextView=itemView.findViewById(R.id.gwidth)
            val gate_height:TextView=itemView.findViewById(R.id.gheight)
            val gate_outer:TextView=itemView.findViewById(R.id.outer_element)
            val gate_inner:TextView=itemView.findViewById(R.id.inner_element)
            val frameCode:TextView=itemView.findViewById(R.id.frame_code)
            itemView.setOnClickListener {
                mctx.startActivity(Intent(mctx,FragmentSelectedFrame::class.java))
            }
        }
    }
    class ViewHolder1(itemview:View):RecyclerView.ViewHolder(itemview) {

        fun bind(model:FrameType){
            val frame1:ImageView =itemView.findViewById(R.id.frame1)
            val frame2:ImageView =itemView.findViewById(R.id.p3frame2)
           // val frame3:ImageView =itemView.findViewById(R.id.frame3)
            val gate_width:TextView=itemView.findViewById(R.id.gwidth)
            val gate_height:TextView=itemView.findViewById(R.id.gheight)
            val gate_outer:TextView=itemView.findViewById(R.id.outer_element)
            val gate_inner:TextView=itemView.findViewById(R.id.inner_element)
            val frameCode:TextView=itemView.findViewById(R.id.frame_code)
        }
    }
    class ViewHolder2(item:View):RecyclerView.ViewHolder(item) {
      fun bind(model:FrameType){

          val frame1:ImageView =itemView.findViewById(R.id.frame1)
        /*  val frame2:ImageView =itemView.findViewById(R.id.frame2)
          val frame3:ImageView =itemView.findViewById(R.id.frame3)*/
          val gate_width:TextView=itemView.findViewById(R.id.gwidth)
          val gate_height:TextView=itemView.findViewById(R.id.gheight)
          val gate_outer:TextView=itemView.findViewById(R.id.outer_element)
          val gate_inner:TextView=itemView.findViewById(R.id.inner_element)
          val frameCode:TextView=itemView.findViewById(R.id.frame_code)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position].type) {
            3 -> {
                FRAME3
            }
            2 -> {
                FRAME2
            }
            else -> {
                FRAME1
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

Log.i("FrameAdapter","VIewtype${viewType}")
      return  when(viewType) {
            FRAME3 -> {
               val item = LayoutInflater.from(mctx).inflate(R.layout.frame_adapter, parent, false)
                ViewHolder(item)
            }
            FRAME2 -> {
                val  item = LayoutInflater.from(mctx).inflate(R.layout.frameadapter01, parent, false)
              ViewHolder1(item)
            }
            FRAME1 -> {
                val item = LayoutInflater.from(mctx).inflate(R.layout.frame_adapter02, parent, false)
                ViewHolder2(item)
            }
          else -> throw IllegalArgumentException("Invalid view type${viewType}")
      }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
/*
        val displayMatrics=DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            val displsy=mctx.applicationContext.display
            displsy?.getRealMetrics(displayMatrics)
        }else{
            @Suppress("DEPRECATION")
            val displsy=(mctx as MainActivity).windowManager.defaultDisplay.getMetrics(displayMatrics)

        }
        val width=displayMatrics.widthPixels
        val itemwidth=width /3.33
       val lp= holder.itemView.(holder.itemView.conta)*/
        val element = itemList[position]
        when(holder){
            is ViewHolder ->{
                holder.bind(element)
            }
            is ViewHolder1->{
                holder.bind(element)
            }
            is ViewHolder2 ->
            {
                holder.bind(element)
            }
        }
    }

}


