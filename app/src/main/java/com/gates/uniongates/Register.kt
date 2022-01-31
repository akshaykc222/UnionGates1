package com.gates.uniongates

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gates.uniongates.objects.RegisterObject
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.loading_screen.*
import kotlin.math.log

class Register : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private val tag="Register"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        loading_View.bringToFront()
        loading_View.visibility=View.GONE
        submit.setOnClickListener {
            if (cname.text!!.isNotEmpty() && cphone.text!!.isNotEmpty() && cphone.text!!.length==10 && address.text!!.isNotEmpty() && adhar_num.text!!.matches(
                    Regex("^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$")
                )&& account_number.text!!.isNotEmpty()&& account_number.text!!.length >9 &&ifscCode.text!!.isNotEmpty()&& ifscCode.text!!.matches(Regex("^[A-Za-z]{4}0[A-Za-z0-9]{6}$"))) {
                val r=RegisterObject(cname.text.toString(),address.text.toString(),cphone.text.toString(),ref_agent_phone.text.toString(),adhar_num.text.toString(),false,account_number.text.toString(),ifscCode.text.toString())
                SaveUsers(r)
            }else{

                if (cname.text!!.isEmpty()){
                    cname.requestFocus()
                    Snackbar.make(it,"Enter name",Snackbar.LENGTH_LONG).show()

                }else if(cphone.text!!.isEmpty()){
                    cphone.requestFocus()
                    Snackbar.make(it,"Enter phone",Snackbar.LENGTH_LONG).show()
                }else if(cphone.text!!.length  !=10){
                    cphone.requestFocus()
                    Snackbar.make(it,"Enter valid phone",Snackbar.LENGTH_LONG).show()
                }else if(address.text!!.isEmpty()){
                    address.requestFocus()
                    Snackbar.make(it,"Enter address",Snackbar.LENGTH_LONG).show()
                }else if (!adhar_num.text!!.matches(Regex("^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$"))){
                    adhar_num.requestFocus()
                    Snackbar.make(it,"Invalid aadhaar number",Snackbar.LENGTH_LONG).show()
                }else if (account_number.text!!.isEmpty()){
                    account_number.requestFocus()
                    Snackbar.make(it,"Enter account number",Snackbar.LENGTH_LONG).show()
                }else if (account_number.text!!.length<9){
                    account_number.requestFocus()
                    Snackbar.make(it,"Invaild account number",Snackbar.LENGTH_LONG).show()
                }
                else if(ifscCode.text!!.isEmpty()){
                    ifscCode.requestFocus()
                    Snackbar.make(it,"Enter ifsc code",Snackbar.LENGTH_LONG).show()
                }else if (!ifscCode.text!!.matches(Regex("^[A-Za--z]{4}0[A-Za-z0-9]{6}$"))){
                    ifscCode.requestFocus()
                    Snackbar.make(it,"Invaild ifsc code",Snackbar.LENGTH_LONG).show()
                }else{
                    Snackbar.make(it,"Enter correct information",Snackbar.LENGTH_LONG).show()
                    Log.d(tag,"una adsddsdsdfsf")
                }
            }

        }
        adhar_num.addTextChangedListener(textWatcher)
    }
    fun SaveUsers(registerob:RegisterObject){
        loading_View.visibility=View.VISIBLE
        val databaseRef=FirebaseDatabase.getInstance().getReference("Register")
        databaseRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.i("akshay",error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(registerob.phone.toString())){
                    Toast.makeText(this@Register,"Already registerd",Toast.LENGTH_LONG).show()
                    loading_View.visibility=View.GONE
                    startActivity(Intent(this@Register,Login::class.java))
                }else{

                    databaseRef.child(registerob.phone.toString()).setValue(registerob).addOnSuccessListener {
                        val intent=Intent(this@Register,ImageUpload::class.java)
                        intent.putExtra("phone",registerob.phone)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        })
    }
    private  val textWatcher=object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d(tag,s.toString())
        }

        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d(tag,"start:$start before :$before count $count")
            if (adhar_num.text!!.length==4 || adhar_num.text!!.length==9){
                adhar_num.setText(adhar_num.text.toString()+" ")
                val pos= adhar_num.text!!.length
                adhar_num.setSelection(pos)
            }
        }

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                Log.d(tag,s.length.toString())
            }
        }

    }
    private val ifscTextWatcher=object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d(tag,"")
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.matches(Regex("[A-Za-z]+\$"))){

            }
        }

        override fun afterTextChanged(s: Editable?) {
           Log.d(tag,"f")
        }

    }
}