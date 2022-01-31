package com.gates.uniongates


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View


import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.gates.uniongates.Adapter.CustomDropDownAdapter

import com.gates.uniongates.Adapter.FrameGallary
import com.gates.uniongates.objects.Frames
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


import kotlinx.android.synthetic.main.loading_screen.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    var  position:Int=0
    private val tag="MainActivity"
    var itemList:ArrayList<Frames> = ArrayList()
    private var itemPos=0
    private var selectedType=0

    // Wheel scrolled flag
    private val wheelScrolled = false
    private var measurementList:ArrayList<String> = arrayListOf("cm,inch,feet,m")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
        loading_View.bringToFront()
        loading_View.setBackgroundColor(Color.WHITE)
        val recycle=findViewById<RecyclerView>(R.id.frame_recycle)
        val adapr=FrameGallary(this@MainActivity,itemList)
        recycle.layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
        recycle.itemAnimator= DefaultItemAnimator()
        recycle.adapter=adapr
        val snapHelper=PagerSnapHelper()
        snapHelper.attachToRecyclerView(recycle)
        addata()
        //initializing types array
        val data = arrayListOf<String>("cm","inch","feet","m")
        //setting cm as default type
//        measurementType.text=data[selectedType]
//
//        //changing values of textview on each touch
//        measurementType.setOnClickListener {
//            selectedType = if (selectedType==data.size-1){
//                0
//            }else{
//                selectedType+1
//            }
//            Log.d("touch",selectedType.toString())
//
//            measurementType.text=data[selectedType]
//
//            position=(frame_recycle.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//            val pos=itemList[position]
//            itemPos=position
//            when(selectedType){
//                0->{
//                    selectedType=0
//                    width.text=itemList[itemPos].width.cm
//                    height.text=itemList[itemPos].height.cm
//                }
//                1->{
//                    selectedType=1
//                    width.text=itemList[itemPos].width.inch
//                    height.text=itemList[itemPos].height.inch
//                }
//                2->{
//                    selectedType=3
//                    width.text=itemList[itemPos].width.feet
//                    height.text=itemList[itemPos].height.feet
//                }
//                3->{
//                    selectedType=4
//                    width.text=itemList[itemPos].width.m
//                    height.text=itemList[itemPos].height.m
//                }
//            }
//        }


//        val types = resources.getStringArray(R.array.measurement_type)
        val adapter = CustomDropDownAdapter(this,data)//spinner adapter
        val wheel: Spinner=findViewById(R.id.measurementType)

        //adapter onclick listner

        switchBTN.setOnToggledListener { toggleableView, isOn ->
                if (isOn){
                    changeUnite("cm",itemList[itemPos])
                }else{
                    changeUnite("feet",itemList[itemPos])
                }
        }
        measurementType.adapter=adapter
        measurementType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                if (itemList.isNotEmpty()){
                    when(position){
                        0->{
                            selectedType=0
                            width.text=itemList[itemPos].width.cm
                            height.text=itemList[itemPos].height.cm
                            changeUnite("cm",itemList[itemPos])
                        }
                        1->{
                            selectedType=1
                            width.text=itemList[itemPos].width.inch
                            height.text=itemList[itemPos].height.inch
                            changeUnite("inch",itemList[itemPos])
                        }
                        2->{
                            selectedType=3
                            width.text=itemList[itemPos].width.feet
                            height.text=itemList[itemPos].height.feet
                            changeUnite("feet",itemList[itemPos])
                        }
                        3->{
                            selectedType=4
                            width.text=itemList[itemPos].width.m
                            height.text=itemList[itemPos].height.m
                            changeUnite("m",itemList[itemPos])
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedType=0
                width.text=itemList[itemPos].width.cm
                height.text=itemList[itemPos].height.cm
                changeUnite("cm",itemList[position])
            }
        }
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        val tot_price=findViewById<TextView>(R.id.totprice)
//        tot_price.text= itemList[0].frame_cost1
        //left right navigation icons
        icon_left.setOnClickListener {
            if (position==0){
                icon_left.isEnabled=false
            }else{
                icon_right.isEnabled=true
                frame_recycle.smoothScrollToPosition(position-1)
            }

        }
        icon_right.setOnClickListener {

            if (position==itemList.size-1){
                icon_right.isEnabled=false
            }else{
                icon_left.isEnabled=true
                frame_recycle.smoothScrollToPosition(position+1)
            }


        }
        frame_recycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState  == RecyclerView.SCROLL_STATE_IDLE){
                    position=(frame_recycle.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val pos=itemList[position]
                    itemPos=position
//                    when(position){
//                        0->{
//                            selectedType=0
//
//                        }
//                        1->{
//                            selectedType=1
//                            width.text=itemList[itemPos].width.inch
//                            height.text=itemList[itemPos].height.inch
//                            changeUnite("inch",itemList[itemPos])
//                        }
//                        2->{
//                            selectedType=3
//                            width.text=itemList[itemPos].width.feet
//                            height.text=itemList[itemPos].height.feet
//                            frameOter.text=pos.frameOuter
//                            frameInner.text=pos.frameInner
//                        }
//                        3->{
//                            selectedType=4
//                            width.text=itemList[itemPos].width.m
//                            height.text=itemList[itemPos].height.m
//                            frameOter.text=pos.frameOuter
//                            frameInner.text=pos.frameInner
//                        }
//                        else->{
//                            changeUnite("inch",itemList[itemPos])
//                        }
//                    }
//                    width.text=pos.width.m
//                    height.text=pos.height.m

                    width.text=itemList[itemPos].width.cm
                    height.text=itemList[itemPos].height.cm
                    changeUnite("cm",itemList[itemPos])
                    val dispText= NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                    //  totprice.text="${dispText.format(frame_properties.frameCost)}"
                    if (pos.frameCost.contains(",")){
                        val price_rem=pos.frameCost.replace(",","")
                        framePrice.text=dispText.format(price_rem.toDouble())
                        // TotPrice=price_rem.toDouble()
                        //frame_cost=price_rem.toDouble()

                    }else{
                        framePrice.text=dispText.format(pos.frameCost.toDouble())
                        // TotPrice=price_rem.toDouble()

                    }
                }
            }
        })

    }

    private fun changeUnite(unit:String,frames:Frames){
        try {
            when(unit){
                "cm"->{
                    //frame outer
                    val currentTxt=frames.frameOuter
                    val txt=currentTxt.split("x")
                    val val1 = txt[0].toFloat()
                    val txt1 = txt[1].split("mm")
                    val val2 = txt1[0].toFloat()
                    Log.d(tag,txt[1])
                    val val1Inch =val1/25.4
                    val val2Inch = val2/25.4
                    val number1digitsOuter:Double = (val1Inch * 100.0).roundToInt() / 100.0
                    val number2digitsOuter:Double = (val2Inch * 100.0).roundToInt() / 100.0
                    frameOter.text="$number1digitsOuter x $number2digitsOuter inch"
                    //frame inner
                    val currentTxtInner=frames.frameInner
                    val txtInner=currentTxt.split("x")
                    val val1Inner = txtInner[0].toFloat()
                    val txt1Inner = txtInner[1].split("mm")
                    val val2Inner = txt1Inner[0].toFloat()
                    Log.d(tag,txt[1])
                    val val1InchInner =val1/25.4
                    val val2InchInner = val2/25.4
                    val number1digits:Double = (val1InchInner * 100.0).roundToInt() / 100.0
                    val number2digits:Double = (val2InchInner * 100.0).roundToInt() / 100.0
                    frameInner.text="$number1digits x $number2digits inch"
                    switchBTN.isOn=true
                }
                "inch"->{
                    //frame outer
                    val currentTxt=frames.frameOuter
                    val txt=currentTxt.split("x")
                    val val1 = txt[0].toFloat()
                    val txt1 = txt[1].split("mm")
                    val val2 = txt1[0].toFloat()
                    Log.d(tag,txt[1])
                    val val1Inch =val1/25.4
                    val val2Inch = val2/25.4
                    val number1digitsOuter:Double = (val1Inch * 100.0).roundToInt() / 100.0
                    val number2digitsOuter:Double = (val2Inch * 100.0).roundToInt() / 100.0
                    frameOter.text="$number1digitsOuter x $number2digitsOuter inch"
                    //frame inner
                    val currentTxtInner=frames.frameInner
                    val txtInner=currentTxt.split("x")
                    val val1Inner = txtInner[0].toFloat()
                    val txt1Inner = txtInner[1].split("mm")
                    val val2Inner = txt1Inner[0].toFloat()
                    Log.d(tag,txt[1])
                    val val1InchInner =val1/25.4
                    val val2InchInner = val2/25.4
                    val number1digits:Double = (val1InchInner * 100.0).roundToInt() / 100.0
                    val number2digits:Double = (val2InchInner * 100.0).roundToInt() / 100.0
                    frameInner.text="$number1digits x $number2digits inch"
                    switchBTN.isOn=true
                }
                "feet"->{
                    frameInner.text=frames.frameInner
                    frameOter.text=frames.frameOuter
                    switchBTN.isOn=false
                }
                "m"->{
                    frameInner.text=frames.frameInner
                    frameOter.text=frames.frameOuter
                    switchBTN.isOn=false
                }
            }
        }catch (e:Exception){
            Log.d(tag,"error$e")
        }

    }
    private fun addata(){

        val dataref=FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = dataref.getReference("Frames")

        myRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                //  Log.i("MainActivity",error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    val snap:HashMap<String,Any> = h.value as HashMap<String, Any>
                    val frameCode=snap["frameCode"] as String
                    //  Log.d(tag,"frameCode : $frameCode")

                    dataref.getReference("images").child(frameCode).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val snap:HashMap<String,Any> =snapshot.value as HashMap<String, Any>
                                val image:String= snap["img"] as String
                                val code:String=snap["name"] as String
                                //Log.d(tag,"frameCode :$frameCode code in images :$code ")

                                val frames=Frames(h,image)
                                itemList.add(frames)
                                //  Log.d(tag,"item list size:${itemList.size}")


                                //val frameImg=snap["i"]
                                // Log.d(tag,)
                            }else{
                                //Log.d(tag,"frameCode :$frameCode imageNotFound ")
                            }
                            // Log.d(tag,"item list outSide:${itemList.size}")
                            frame_recycle.adapter?.notifyDataSetChanged()
                            if (itemList.isNotEmpty()){
                                width.text=itemList[0].width.cm
                                height.text=itemList[0].height.cm
                                val dispText= NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                                //  totprice.text="${dispText.format(frame_properties.frameCost)}"
                                if (itemList[0].frameCost.contains(",")){
                                    val price_rem=itemList[0].frameCost.replace(",","")
                                    framePrice.text=dispText.format(price_rem.toDouble())
                                    // TotPrice=price_rem.toDouble()
                                    //frame_cost=price_rem.toDouble()

                                }else{
                                    framePrice.text=dispText.format(itemList[0].frameCost.toDouble())
                                    // TotPrice=price_rem.toDouble()

                                }
//                                frameInner.text=itemList[0].frameInner
//                                frameOter.text=itemList[0].frameOuter
                                changeUnite("cm",itemList[0])
                            }
                            loading_View.visibility= View.GONE
                        }

                        override fun onCancelled(error: DatabaseError) {
                            //Log.d(tag,error.toString())
                        }

                    })


                    // itemList.add(obj)
                }


            }



        })
    }
}
