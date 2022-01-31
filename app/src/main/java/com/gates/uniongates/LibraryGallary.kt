package com.gates.uniongates

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gates.uniongates.Adapter.LibraryAdapter
import com.gates.uniongates.objects.Gates
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_library_gallary.*
import kotlinx.android.synthetic.main.activity_library_gallary.view.*
import kotlinx.android.synthetic.main.loading_screen.*

class LibraryGallary : AppCompatActivity() {
    val tag="LibraryGallary"
    val libraryList:ArrayList<Gates> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_library_gallary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)
        }
        toolbarlg.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbarlg.setNavigationOnClickListener {
            startActivity(Intent(this,Interduction::class.java))
        }
        loading_View.visibility= View.VISIBLE
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        val bundle=intent.extras
        val range=bundle?.getString("range")
        val recycle = findViewById<RecyclerView>(R.id.liberaryRecycle)
        val adaper = LibraryAdapter(this@LibraryGallary,libraryList,libraryList)
        recycle.layoutManager = LinearLayoutManager(this@LibraryGallary,LinearLayoutManager.VERTICAL,false)
        recycle.itemAnimator= DefaultItemAnimator()
        recycle.adapter=adaper
        getLibraryItems(range.toString())
        if (range=="80000"){
            filter.visibility=View.GONE
        }
       filter.setOnClickListener { it ->
           val popupmenu=PopupMenu(this,it)
           popupmenu.inflate(R.menu.popless3)
           when(range){
               "30000"-> {
                   popupmenu.menu.setGroupVisible(R.id.pop30000grp, true)

               }
               "50000"->popupmenu.menu.setGroupVisible(R.id.pop50000grp,true)
               "70000"->popupmenu.menu.setGroupVisible(R.id.pop70000grp,true)
               else->{
                   filter.visibility=View.GONE
               }
           }
           popupmenu.setOnMenuItemClickListener {menu_item->
               when(menu_item.itemId){
                   R.id.pop10000->{
                       if (!adaper.filter(0.0, 10000.0)){
                           error_txt.visibility=View.VISIBLE
                       }else{
                           error_txt.visibility=View.GONE
                       }

                   }
                   R.id.pop20000->{
                       if (!adaper.filter(10000.0, 20000.0)){
                           error_txt.visibility=View.VISIBLE
                       }else{
                           error_txt.visibility=View.GONE
                       }
                   }
                   R.id.pop30000->{
                       if (!adaper.filter(20000.0, 30000.0)){
                           error_txt.visibility=View.VISIBLE
                       }else{
                           error_txt.visibility=View.GONE
                       }
                   }
                   R.id.pop40000->{
                       if (!adaper.filter(30000.0, 40000.0)){
                           error_txt.visibility=View.VISIBLE
                       }else{
                           error_txt.visibility=View.GONE
                       }
                   }
                   R.id.pop50000->{
                       if (!adaper.filter(40000.0, 50000.0)){
                           error_txt.visibility=View.VISIBLE
                       }else{
                           error_txt.visibility=View.GONE
                       }
                   }
                   R.id.pop60000->{
                       if (!adaper.filter(50000.0, 60000.0)){
                           error_txt.visibility=View.VISIBLE
                       }else{
                           error_txt.visibility=View.GONE
                       }
                   }
                   R.id.pop70000->{
                       if (!adaper.filter(60000.0, 70000.0)){
                           error_txt.visibility=View.VISIBLE
                       }else{
                           error_txt.visibility=View.GONE
                       }
                   }

               }
               return@setOnMenuItemClickListener true
           }

           popupmenu.show()
       }
    }
    private fun getLibraryItems(range:String){
        val firebaseDatabase=FirebaseDatabase.getInstance().getReference("Gates/$range")
        firebaseDatabase.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.d(tag, "firebase on cancelled error:$error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(tag,"snapshot length ${snapshot.key }}")
                if (snapshot.exists()){
                    for (h in snapshot.children){
                        val gates=Gates(h)
                        libraryList.add(gates)
                    }
                    liberaryRecycle.adapter?.notifyDataSetChanged()
                    loading_View.visibility=View.GONE
                }
            }

        })
    }
}