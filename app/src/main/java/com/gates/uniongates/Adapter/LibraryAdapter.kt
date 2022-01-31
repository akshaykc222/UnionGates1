package com.gates.uniongates.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gates.uniongates.R
import com.gates.uniongates.RequestGate
import com.gates.uniongates.objects.Gates

class LibraryAdapter(private val mctx: Context,private val libraryList:ArrayList<Gates>,private val recoveryList:ArrayList<Gates>):RecyclerView.Adapter<LibraryAdapter.ViewHolder>(){
    companion object{
        val tempList:ArrayList<Gates> =ArrayList()
    }
    class ViewHolder(item:View):RecyclerView.ViewHolder(item) {
        val gateImage:ImageView=item.findViewById(R.id.gateimg)
        val gateName:TextView=item.findViewById(R.id.gatename)
        val gateDesc:TextView=item.findViewById(R.id.gate_desc)
        val gateCost:TextView=item.findViewById(R.id.gatecost)
        val buy:Button=item.findViewById(R.id.buy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item=LayoutInflater.from(mctx).inflate(R.layout.gate_gallary_adapter,parent,false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return libraryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val h=libraryList[position]
        Glide.with(mctx).load(h.gateImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(holder.gateImage)
        holder.gateName.text=h.gateName
        holder.gateCost.text=h.gateCost
        holder.gateDesc.text=h.gateDesc
        holder.buy.setOnClickListener {
            val intent= Intent(mctx,RequestGate::class.java)
            intent.putExtra("gateId",h.gateId)
            mctx.startActivity(intent)
        }

    }

    fun filter(from:Double,to:Double):Boolean {
        Log.d("Adapte",recoveryList.size.toString())
        tempList.addAll(libraryList)
        libraryList.clear()
        for (item in tempList) {
            if (item.gateCost!!.toDouble() in from..to) {
                libraryList.add(item)
            }

        }
        val removeList= libraryList.distinctBy { obj->obj.gateId }
        libraryList.clear()
        libraryList.addAll(removeList)
        notifyDataSetChanged()
        return libraryList.size != 0
    }
}