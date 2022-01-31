package com.gates.uniongates

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gates.uniongates.objects.Frames
import com.gates.uniongates.objects.Panel
import com.gates.uniongates.objects.SelImage
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_selected_frame2.*
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.ArrayList
import android.content.ContextWrapper
import android.graphics.Color
import android.icu.number.NumberFormatter
import android.os.Build
import android.provider.CalendarContract
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gates.uniongates.Adapter.CustomDropDownAdapter
import com.gates.uniongates.Adapter.ModelBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_interduction.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_selected_frame2.banner
import kotlinx.android.synthetic.main.activity_selected_frame2.dispText
import kotlinx.android.synthetic.main.activity_selected_frame2.frameInner
import kotlinx.android.synthetic.main.activity_selected_frame2.frameOter
import kotlinx.android.synthetic.main.activity_selected_frame2.gate_cost_text
import kotlinx.android.synthetic.main.activity_selected_frame2.height
import kotlinx.android.synthetic.main.activity_selected_frame2.measurementType
import kotlinx.android.synthetic.main.activity_selected_frame2.width
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap


class SelectedFrameActivity : AppCompatActivity() {
    private var sel_fragment:String = ""
    private val panel_tag="FRAGMENT1of1"
    private val panel_tag2="FRAGMENT2of1"
    private val panel_tag2_1="FRAGMENT2of2"
    private val panel_tag3_1="FRAGMENT3of1"
    private val panel_tag3_2="FRAGMENT3of2"
    private val panel_tag4_1="FRAGMENT4of1"
    private val panel_tag4_2="FRAGMENT4of2"
    private var selectedType=0
    private val filepath = "Desingns"
    private var selected_frame_num_panels:Int=0
    private var frame_cost:Double=0.0
    private var Selected_Panels:ArrayList<SelImage> = ArrayList()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_selected_frame2)
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)
        //  supportFragmentManager.beginTransaction().replace(R.id.frame_layout,FragmentTypeSelected()).commit()
        val bundle=intent.extras
        val frame_properties:Frames= bundle?.getSerializable("proprties") as Frames
        val sessionManager=SessionManager(this)
        if(sessionManager.GetLang() == "ml"){
            Glide.with(this).load(ContextCompat.getDrawable(this,R.drawable.banner_ml)).into(banner)
        }else{
            Glide.with(this).load(ContextCompat.getDrawable(this,R.drawable.banner_eng)).into(banner)
        }

       // totprice.text="₹\t"+frame_properties.frameCost
        frameInner.text=frame_properties.frameInner
        frameOter.text=frame_properties.frameOuter
        width.text=frame_properties.width.m
        height.text=frame_properties.height.m
        val price_rem:String
        //setting text of gate now costs
        gate_cost_text.text = resources.getString(R.string.your_frame_now_cost)
        val dispText=NumberFormat.getCurrencyInstance(Locale("en", "IN"))
      //  totprice.text="${dispText.format(frame_properties.frameCost)}"
        if (frame_properties.frameCost.contains(",")){
            price_rem=frame_properties.frameCost.replace(",","")
         totprice.text=dispText.format(price_rem.toDouble())
           // TotPrice=price_rem.toDouble()
            frame_cost=price_rem.toDouble()

        }else{
            totprice.text=dispText.format(frame_properties.frameCost.toDouble())
            // TotPrice=price_rem.toDouble()
            frame_cost=frame_properties.frameCost.toDouble()
        }
        toolbar.setNavigationOnClickListener { 
            startActivity(Intent(this,MainActivity::class.java))
        }
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
//            when(selectedType){
//                        0->{
//                            selectedType=0
//                            width.text=frame_properties.width.cm
//                            height.text=frame_properties.height.cm
//                        }
//                        1->{
//                            selectedType=1
//                            width.text=frame_properties.width.inch
//                            height.text=frame_properties.height.inch
//                        }
//                        2->{
//                            selectedType=3
//                            width.text=frame_properties.width.feet
//                            height.text=frame_properties.height.feet
//                        }
//                        3->{
//                            selectedType=4
//                            width.text=frame_properties.width.m
//                            height.text=frame_properties.height.m
//                        }
//                    }
//        }

//        val types = resources.getStringArray(R.array.measurement_type)
        val adapter = CustomDropDownAdapter(this,data)//spinner adapter
        measurementType.adapter=adapter
        //adapter onclick listner
        measurementType.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                    when(position){
                        0->{
                            selectedType=0
                            width.text=frame_properties.width.cm
                            height.text=frame_properties.height.cm
                        }
                        1->{
                            selectedType=1
                            width.text=frame_properties.width.inch
                            height.text=frame_properties.height.inch
                        }
                        2->{
                            selectedType=3
                            width.text=frame_properties.width.feet
                            height.text=frame_properties.height.feet
                        }
                        3->{
                            selectedType=4
                            width.text=frame_properties.width.m
                            height.text=frame_properties.height.m
                        }
                    }
                }


            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedType=0
                width.text=frame_properties.width.cm
                height.text=frame_properties.height.cm
            }
        }
        viewFull.setOnClickListener {
//            val modelBottomSheet=ModelBottomSheet()
//            modelBottomSheet.show(supportFragmentManager,"ModalBottomSheet")

            viewFull.isEnabled=false

            if (selected_frame_num_panels == Selected_Panels.size) {

                val outputStream: FileOutputStream?
                val cw = ContextWrapper(applicationContext)
                // val file: File =  Environment.getExternalStorageDirectory()
                val dir = cw.getDir("Designs", Context.MODE_PRIVATE)
                dir.mkdirs()
                val filename = String.format("%d.png", System.currentTimeMillis())
                val outFile = File(dir, filename)
                outputStream = FileOutputStream(outFile)
                Log.i("akshay", outFile.toString())
                val bitmap = viewToBitmap(frame_layout)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                val sessionManager = SessionManager(this@SelectedFrameActivity)
                //  sessionManager.saveCurentSel()
                val intetn = Intent(this@SelectedFrameActivity, FinalFrame::class.java)
                intetn.putExtra("finalframe", outFile.toString())
                intetn.putExtra("selectedp", Selected_Panels)
                intetn.putExtra("price", totprice.text.toString())
                intetn.putExtra("frame", frame_properties.frameCode)
                startActivity(intetn)

            } else {
                viewFull.isEnabled=true
                Snackbar.make(it, "Select panels to continue", Snackbar.LENGTH_LONG).show()
            }
        }
        save.setOnClickListener {
            val bottomSheetDialog=BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.custom_layout)
            val proceed=bottomSheetDialog.findViewById<AppCompatButton>(R.id.proceed)
            proceed?.setOnClickListener {
                save.isEnabled=false
                CoroutineScope(Dispatchers.IO).launch {
                    is_account_activated(object :AccountActiveted{
                        override fun onAccountReceived(allowToOrder: Boolean) {
                            if (allowToOrder){
                                CoroutineScope(Dispatchers.Main).launch {
                                    Log.i(
                                        "TAG",
                                        selected_frame_num_panels.toString() + "panel size" + Selected_Panels.size
                                    )
                                    if (selected_frame_num_panels == Selected_Panels.size) {

                                        val outputStream: FileOutputStream?
                                        val cw = ContextWrapper(applicationContext)
                                        // val file: File =  Environment.getExternalStorageDirectory()
                                        val dir = cw.getDir("Designs", Context.MODE_PRIVATE)
                                        dir.mkdirs()
                                        val filename = String.format("%d.png", System.currentTimeMillis())
                                        val outFile = File(dir, filename)
                                        outputStream = FileOutputStream(outFile)
                                        Log.i("akshay", outFile.toString())
                                        val bitmap = viewToBitmap(frame_layout)
                                        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                        outputStream.flush()
                                        outputStream.close()
                                        val sessionManager = SessionManager(this@SelectedFrameActivity)
                                        //  sessionManager.saveCurentSel()
                                        val intetn = Intent(this@SelectedFrameActivity, Cutomer_Details::class.java)
                                        intetn.putExtra("finalframe", outFile.toString())
                                        intetn.putExtra("selectedp", Selected_Panels)
                                        intetn.putExtra("price", totprice.text.toString())
                                        intetn.putExtra("frame", frame_properties.frameCode)
                                        startActivity(intetn)

                                    } else {
                                        save.isEnabled=true
                                        Snackbar.make(it, "Select panels to continue", Snackbar.LENGTH_LONG).show()
                                    }

                                }
                            }else{
                                save.isEnabled=true
                                Snackbar.make(save,"Sorry you can't order right now.Please contact admin",Snackbar.LENGTH_LONG).show()
                            }
                        }

                        override fun onError(error: String) {
                            Snackbar.make(it,error,Snackbar.LENGTH_LONG).show()
                        }

                    })
                }
            }
            bottomSheetDialog.show()

        //  Toast.makeText(this,"file saved", Toast.LENGTH_LONG).show()

            }
            /*selected frame properties setting fragment of selected frame*/
            when (frame_properties.panels) {
                "1" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, Fragment1of1(), panel_tag).commit()
                    sel_fragment = panel_tag
                    selected_frame_num_panels = 1
                }
                "2" -> {
                    sel_fragment = if (frame_properties.is1200) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, Fragment2of2(), panel_tag2_1).commit()
                        panel_tag2_1
                    } else {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, Fragment2of1(), panel_tag2).commit()
                        panel_tag2
                    }
                    selected_frame_num_panels = 2
                }
                "3" -> {
                    sel_fragment = if (frame_properties.is1200) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, Fragment3of2(), panel_tag3_2).commit()
                        panel_tag3_2
                    } else {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, Fragment3of1(), panel_tag3_1).commit()
                        panel_tag3_1
                    }
                    selected_frame_num_panels = 3
                }
                "4" -> {
                    sel_fragment = if (frame_properties.is300) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, Fragment4of2(), panel_tag4_2).commit()
                        panel_tag4_2
                    } else {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, Fragment4of1(), panel_tag4_1).commit()
                        panel_tag4_1
                    }
                    selected_frame_num_panels = 4
                }

            }

        //end of when block
        /*getting number of panel from the selected frame */

        //end of when block
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==20){
            gate_cost_text.text=resources.getText(R.string.your_gate_now_costs)
            var TotPrice=0.0
            var price:Double=0.0
            val sel_img=data?.getIntExtra("SEL_PANEL",0)
            val gh:Panel=data?.getSerializableExtra("PANEL_PROPERTIES")  as Panel
            //changing text


            if (gh.panel_cost?.contains(",")!!){
                val removed_STR=gh.panel_cost?.replace(",","")
                price = removed_STR?.toDouble()!!
            }else{
                price = gh.panel_cost!!.toDouble()
            }
            val selImage=SelImage(sel_img?.toInt()!!, gh.panelCode.toString(),price)
            if (Selected_Panels.size==0){
                Selected_Panels.add(selImage)

            }else{
                for (i in 0 until  Selected_Panels.size){
                    val obj=Selected_Panels[i]
                    if (obj.ImagePosition==sel_img){
                       // TotPrice-=obj.Panel_cost
                        Selected_Panels.removeAt(i)
                      break

                    }
                }
                Selected_Panels.add(selImage)
            }
            var tempCost=0.0
            for ( i in  Selected_Panels){
                Log.i("TAG","total price${TotPrice}:panel cost${i.Panel_cost} image pos${i.ImagePosition} array size${Selected_Panels.size}")
                tempCost+=i.Panel_cost
            }
            tempCost *= 2
            tempCost+=frame_cost
            TotPrice+=tempCost
            val dispText=NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            totprice.text=dispText.format(TotPrice)
            if (selected_frame_num_panels==Selected_Panels.size){
               showwar.visibility=View.GONE
                gate_cost_text.text=resources.getText(R.string.your_gate_now_costs)
                visibleActions()
            }
          //  selected_frames.append("\npanel $sel_img : ${gh.panel_cost}\n")

            when(sel_fragment){

                panel_tag-> {
                    val f1 = supportFragmentManager.findFragmentByTag(panel_tag) as Fragment1of1
                    f1.updateImage(gh.panel_img.toString())
                }
                panel_tag2->{
                    val f1=  supportFragmentManager.findFragmentByTag(panel_tag2) as Fragment2of1
                    f1.updateImage(gh.panel_img.toString(), sel_img)
                }
                panel_tag2_1->{
                    val f1=  supportFragmentManager.findFragmentByTag(panel_tag2_1) as Fragment2of2
                    f1.updateImage(gh.panel_img.toString(), sel_img)
                    Log.i("akshay",gh.panel_img.toString()+"\t"+sel_img)
                }
                panel_tag3_1->{
                    val f1=  supportFragmentManager.findFragmentByTag(panel_tag3_1) as Fragment3of1
                    f1.updateImage(gh.panel_img.toString(), sel_img)
                }
                panel_tag3_2->{
                    val f1=  supportFragmentManager.findFragmentByTag(panel_tag3_2) as Fragment3of2
                    f1.updateImage(gh.panel_img.toString(), sel_img)
                }
                panel_tag4_1->{
                    val f1=  supportFragmentManager.findFragmentByTag(panel_tag4_1) as Fragment4of1
                    f1.updateImage(gh.panel_img.toString(), sel_img)
                }
                panel_tag4_2->{
                    val f1=  supportFragmentManager.findFragmentByTag(panel_tag4_2) as Fragment4of2
                    f1.updateImage(gh.panel_img.toString(), sel_img)
                }
            }
            //save the design
            //val bitmap=viewToBitmap(frame_layout)
           // Toast.makeText(this,bitmap.toString(),Toast.LENGTH_LONG).show()
          //  Log.i("akshay",bitmap.toString())
           // bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                //Toast.makeText(this,sel_fragment.id,Toast.LENGTH_LONG).show()


         /*   when(sel_img){
                0 -> Toast.makeText(this,"please try again", Toast.LENGTH_LONG).show()
                1 -> { Glide.with(this).load(gh.panel_cost).into(frame1)}
                2 -> { Glide.with(this).load(gh.panel_cost).into(frame2)}
                3 -> { Glide.with(this).load(gh.panel_cost).into(frame3)}
            }*/

//            Toast.makeText(this,"result getted"+gh.panel_cost, Toast.LENGTH_LONG).show()
        }
    }
    //this fuction is used to convert view into bitmap
   private fun viewToBitmap(view: View): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    private fun visibleActions(){
        save.visibility=View.VISIBLE
        viewFull.visibility=View.VISIBLE
        dispText.text=resources.getText(R.string.caution_text)
        dispText.setTextColor(Color.RED)
        dispText.textSize= 16F
    }
    suspend fun is_account_activated(listener:AccountActiveted){

        // val intent:Intent
        val sessionManager=SessionManager(this);
        val phoneWithCountryCode =sessionManager.GetPhone()
        var phone=""
        if (phoneWithCountryCode.length==13){
            phone=phoneWithCountryCode.replace("+91","")
            Log.d("SplashScreen", phone)
        }

        var isActivated:Boolean=false
        Log.d("Login",phone.toString())
        val ref= FirebaseDatabase.getInstance().getReference("Register/$phone")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.i("Login",error.toString())
                listener.onError(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Log.i("Login",snapshot.key.toString())

                    val json:HashMap<String,Any> = snapshot.value as HashMap<String, Any>
                    if (json.containsKey("orderAllowed")){
                        if (json["isApproved"]==true && json["orderAllowed"]==true){
                            listener.onAccountReceived(true)
                        }else{
                            listener.onAccountReceived(false)
                        }
                    }else{
                        listener.onAccountReceived(false)
                    }



                    /*   val gh=RegisterObject(snapshot)
                       if (gh!!){
                        startActivity(Intent(this@Login,MainActivity::class.java))
                    }else{
                        startActivity(Intent(this@Login,FailedActivation::class.java))
                    }*/
                    // Log.i("Login",h.toString())



                    //isActivated=gh.IsApproved

                }else{
                    listener.onError("No data found")
                }
            }

        })

    }

}
