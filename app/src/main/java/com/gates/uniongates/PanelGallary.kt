package com.gates.uniongates

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gates.uniongates.Adapter.FrameGallary
import com.gates.uniongates.Adapter.PanelGallaryAdapter
import com.gates.uniongates.Adapter.PanelTypesAdapter
import com.gates.uniongates.objects.Frames
import com.gates.uniongates.objects.Panel
import com.gates.uniongates.objects.PanelGallary
import com.gates.uniongates.objects.SessionSave
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_panel_gallary.*
import kotlinx.android.synthetic.main.activity_panel_selection.*
import kotlinx.android.synthetic.main.loading_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PanelGallary : AppCompatActivity(){
    private var SelectedRange:String=""
    private var ISrangeSelected=false
    var panel_size=0
    var SelImg=0
    private var tag="PanelCode"
    private var GallaryArray:ArrayList<Panel> = ArrayList()
    private var panelTypeList:ArrayList<String> = ArrayList()
    var selectedType=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_panel_gallary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)
        }
        loading_View.visibility=View.GONE
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        panelTypeRecyler.adapter=PanelTypesAdapter(this,panelTypeList,object :PanelTypeOnclick{
            override fun onClick(index: Int) {
              loadPrice(panelTypeList[index])
            }

        })
        panelTypeRecyler.layoutManager= GridLayoutManager(this@PanelGallary,2)
        panelTypeRecyler.itemAnimator= DefaultItemAnimator()
        loadPanelTypes()


        val bundle=intent.extras
        SelImg= bundle?.getInt("imageview")!!
        panel_size= bundle.getInt("size")
        //saving the values to session for temp usage
        val sessionManager=SessionManager(this)
        sessionManager.saveCurentSel(SessionSave(SelImg,panel_size))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }
    private fun loadPrice(type:String){
        panel_selection.visibility=View.GONE
        val typ:String=type.replace(".","")

try {
    loading_View.visibility= View.VISIBLE
    val database = FirebaseDatabase.getInstance()
    Log.d("PanelCode","panel$typ panelSize $panel_size")
    val myRef: DatabaseReference = database.getReference("Panels").child(typ).child(panel_size.toString())
    myRef.addValueEventListener(object :ValueEventListener{
        override fun onCancelled(error: DatabaseError) {
            Log.i("PanelGallary",error.toString())
        }

        override fun onDataChange(snapshot: DataSnapshot) {
         if (snapshot.exists()){
             for (h in snapshot.children){
                 Log.i("panelgallary",h.key.toString())
                 val snap:HashMap<String,Any> = h.value as HashMap<String, Any>
                 val panelCode=snap["panelCode"] as String
                 Log.d("PanelCode","frameCode : $panelCode")

                 FirebaseDatabase.getInstance().getReference("images").child(panelCode).addListenerForSingleValueEvent(object :ValueEventListener{
                     override fun onDataChange(snapshot: DataSnapshot) {
                         if (snapshot.exists()) {
                             val snap:HashMap<String,Any> =snapshot.value as HashMap<String, Any>
                             val image:String= snap["img"] as String
                             val code:String=snap["name"] as String
                             Log.d(tag,"frameCode :$panelCode code in images :$code ")

                             val panel=Panel(h,image)
                             GallaryArray.add(panel)
                             Log.i("panelgallary",GallaryArray.size.toString())
                             Log.d(tag,"item list size:${GallaryArray.size}")


                             //val frameImg=snap["i"]
                             // Log.d(tag,)
                         }else{
                             Log.d(tag,"frameCode :$panelCode imageNotFound ")
                         }
                         Log.d(tag,"item list outSide:${GallaryArray.size}")
                         gallaryrecycle.adapter?.notifyDataSetChanged()

                         loading_View.visibility= View.GONE
                     }

                     override fun onCancelled(error: DatabaseError) {
                         Log.d(tag,error.toString())
                     }

                 })

             }
         }else{
             Toast.makeText(this@PanelGallary,"No Data Found",Toast.LENGTH_LONG).show()
         }
            val recycle=findViewById<RecyclerView>(R.id.gallaryrecycle)
            val adapr= PanelGallaryAdapter(this@PanelGallary,GallaryArray,SelImg)
            recycle.layoutManager= GridLayoutManager(this@PanelGallary,2)
            recycle.itemAnimator= DefaultItemAnimator()
            recycle.setItemViewCacheSize(GallaryArray.size)
            recycle.adapter=adapr
            loading_View.visibility=View.GONE
        }

    })
}catch (e:Exception){
    Log.i("akshay",e.toString())
}




    }

 private fun loadPanelTypes(){
     CoroutineScope(Dispatchers.IO).launch {
         loading_View.visibility=View.VISIBLE
         FirebaseDatabase.getInstance().getReference("panelTypes").addValueEventListener(object :ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()){
                     for (p in snapshot.children){
                         val panelType=p.value as HashMap<String,Any>
                         panelTypeList.add(panelType["type"] as String)
                     }
                     loading_View.visibility=View.GONE
                     panelTypeRecyler.adapter?.notifyDataSetChanged()
                 }

             }

             override fun onCancelled(error: DatabaseError) {
                 Log.d("PanelGallery",error.toString())
             }

         })
     }

 }
    fun Finish(){
        finish()
    }

    override fun onBackPressed() {
        if (panel_selection.visibility==View.GONE){
            GallaryArray.clear()
            panel_selection.visibility=View.VISIBLE
        }else{
            super.onBackPressed()
        }
    }
    fun setSelected(type:String){
        selectedType=type
        Log.d("PanelType",selectedType)
    }
}