package com.gates.uniongates

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gates.uniongates.objects.Customer
import kotlinx.android.synthetic.main.activity_order_placed.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class OrderPlaced : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        val bundle=intent.extras
        val cObj= bundle?.getSerializable("customer") as Customer
        sendSms(cObj.phone,cObj.agent)
        go_home.setOnClickListener {
            finishAffinity()

        }
    }
    private fun sendSms(cPhone:String,aPhone:String){
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val client = OkHttpClient();

                val mediaType = MediaType.parse("application/json");
                val body = RequestBody.create(
                    mediaType,
                    "{\"messages\": [{\"source\": \"mashape\",\"from\": \"Union gates\",\"body\": \"Thank you for your Patronage.We shall confirm the order details with drawing and payment procedure via Whatsapp.Please note that all the payments are to be made through Bank Account only.The Company will not be responsible/liable to any cash dealing with the Agent.Thank you\",\"to\": \"+91${cPhone}\",\"schedule\": \"1452244637\",\"custom_string\": \"this is a test\" }," +
                            "{\"source\": \"mashape\",\"from\": \"Union gates\",\"body\": \"Thank you for your Patronage.We shall confirm the order details with drawing and payment procedure via Whatsapp.Please note that all the payments are to be made through Bank Account only.The Company will not be responsible/liable to any cash dealing with the Agent.Thank you\",\"to\": \"${aPhone}\",\"schedule\": \"1452244637\",\"custom_string\": \"this is a test\" }]}"
                );
                val request = Request.Builder()
                    .url("https://clicksend.p.rapidapi.com/sms/send")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader(
                        "authorization",
                        "Basic YW50b255dmFAdW5pb25nYXRlcy5jb206QTYxMUIzRTQtOTkxNS0yMjBCLTQ4MTMtN0FBOUMyNDIxQzI3"
                    )
                    .addHeader(
                        "x-rapidapi-key",
                        "7535869362msh9fded6922cf31fcp1ae8a4jsna2928b78def8"
                    )
                    .addHeader("x-rapidapi-host", "clicksend.p.rapidapi.com")
                    .build();

                val response = client.newCall(request).execute()
              //  Log.d("sms", response.body().string())
            }

        }
        }
}