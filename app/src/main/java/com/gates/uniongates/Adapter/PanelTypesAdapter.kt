package com.gates.uniongates.Adapter

import android.app.Activity
import android.content.Context
import android.service.autofill.TextValueSanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gates.uniongates.PanelTypeOnclick
import com.gates.uniongates.R
import kotlinx.android.synthetic.main.panel_type_adapter.view.*

class PanelTypesAdapter(private val mctx: Context, private val panelTypes:ArrayList<String>,val listner:PanelTypeOnclick):RecyclerView.Adapter<PanelTypesAdapter.ViewHolder>() {
    class ViewHolder(item:View):RecyclerView.ViewHolder(item) {
        val panelType:TextView=item.panelType

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item=LayoutInflater.from(mctx).inflate(R.layout.panel_type_adapter,parent,false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.panelType.text=panelTypes[position]
        holder.itemView.setOnClickListener {
            listner.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return panelTypes.size
    }

}