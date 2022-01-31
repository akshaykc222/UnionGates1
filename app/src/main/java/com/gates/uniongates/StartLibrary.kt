package com.gates.uniongates

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_start_library.*

class StartLibrary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_library)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)
        }
        toolbar2.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar2.setNavigationOnClickListener {
            val intent=Intent(this,Interduction::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        range30.setOnClickListener {
            val intent=Intent(this,LibraryGallary::class.java)
            intent.putExtra("range","30000")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        range50.setOnClickListener {
            val intent=Intent(this,LibraryGallary::class.java)
            intent.putExtra("range","50000")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        range70.setOnClickListener {
            val intent=Intent(this,LibraryGallary::class.java)
            intent.putExtra("range","70000")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
      range8.setOnClickListener {
          val intent=Intent(this,LibraryGallary::class.java)
          intent.putExtra("range","80000")
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
          startActivity(intent)
      }
    }
}