package com.gates.uniongates

import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.gates.uniongates.Adapter.IntroSlider
import kotlinx.android.synthetic.main.activity_intro_to_app.*
import java.util.*
import kotlin.collections.ArrayList


class IntroToApp : AppCompatActivity() {
    var sessionManager: SessionManager? =null
    lateinit var locale: Locale
    var flag=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_to_app)
        //hiding status bar
        //window.decorView.systemUiVisibility
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
       /* if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            }else{
            this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }*/

        tablayout.bringToFront()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         window.statusBarColor = ContextCompat.getColor(this, R.color.grey)

        }
        sessionManager= SessionManager(this)
     /*   if (sessionManager!!.GetLang()=="1"){
            showLanguageAlert()
        }else{
            Log.d("tag", flag.toString())

            setLocale(sessionManager!!.GetLang(),1)

            //recreate()
        }*/
        val images:ArrayList<Int> = arrayListOf(R.drawable.text1,R.drawable.text2,R.drawable.text3)
        val adapter=IntroSlider(this,images)
        viewPager.adapter=adapter
      //  pageIndicatorView.count=images.size
       // pageIndicatorView.selection=1
      /*  viewPager.requestDisallowInterceptTouchEvent(true)*/

        viewPager.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val x=event?.x
               // val y=event?.y
                val displayMetrics=DisplayMetrics()
                var height=0
                var width=0

                Log.d("change", "clicked ${event?.action}")

                    when (event?.action){

                        MotionEvent.ACTION_UP->{
                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                                val windowMetrics=this@IntroToApp.windowManager.currentWindowMetrics
                                val insets=windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                                height=windowMetrics.bounds.height()
                                width=windowMetrics.bounds.width()
                            }else{
                                windowManager.defaultDisplay.getMetrics(displayMetrics)
                                height=displayMetrics.heightPixels
                                width=displayMetrics.widthPixels
                            }
                            val leftpercentage=(width)*20/100
                            if (x!! <= leftpercentage){
                                Log.d("changes", viewPager.currentItem.toString()+"viewpager current item")
                                viewPager.setCurrentItem(viewPager.currentItem-1,true)
                            }else{
                                if (viewPager.currentItem==images.size-1){
                                    startActivity(Intent(this@IntroToApp,Login::class.java))
                                }
                                Log.d("changes", viewPager.currentItem.toString()+"viewpager current item")
                                viewPager.setCurrentItem(viewPager.currentItem+1,true)

                            }
                            return false
                        }
                        else->{

                        }
                    }


                return true
            }

        })
        tablayout.setupWithViewPager(viewPager,true)
        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("IntroToApp","position $position image size: ${images.size}")
                if (position==images.size-1){
                    skip.text= resources.getString(R.string.continue_to)
                }else{
                    skip.text= resources.getString(R.string.skip)
                }
               // pageIndicatorView.selection=position
            }

            override fun onPageSelected(position: Int) {

            }

        })

      /*  next.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem-1,true)
            if (viewPager.currentItem==images.size-1){
                skip.text="Continue"
            }
        }
        prev.setOnClickListener {

            viewPager.setCurrentItem(viewPager.currentItem+1,true)
        }*/
        skip.setOnClickListener{
            startActivity(Intent(this,Interduction::class.java))
        }
    }
    private fun showLanguageAlert(){
        val listItems= arrayOf("English","മലയാളം")
        val alertDialogBuilder= AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Select Language")
        alertDialogBuilder.setSingleChoiceItems(listItems,-1,object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    0->{
                        setLocale("en",0)
                        recreate()
                    }
                    1->{
                        setLocale("ml",0)
                        recreate()
                    }
                }
                dialog?.dismiss()
            }

        })
        val alertDialog=alertDialogBuilder.create()
        alertDialog.show()
    }
    fun getSystemLocaleLegacy(config: Configuration): Locale? {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getSystemLocale(config: Configuration): Locale? {
        return config.locales[0]
    }

    fun setSystemLocaleLegacy(config: Configuration, locale: Locale?) {
        config.locale = locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun setSystemLocale(config: Configuration, locale: Locale?) {
        config.setLocale(locale)
    }
    private fun setLocale(s: String,from:Int) {
        sessionManager?.Savelan(s)
        locale = Locale(s)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)

        flag=false

        //  startActivity(Intent(this,Interduction::class.java))

    }
}