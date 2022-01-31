package com.gates.uniongates

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.gates.uniongates.objects.ProffImage
import com.gates.uniongates.objects.ProofImage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_image_upload.*
import kotlinx.android.synthetic.main.loading_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class ImageUpload : AppCompatActivity() {
    private val GAllAYPICK=10
    private val GAllAYPICK1=20
    private val GAllAYPICK3=30
   var uploadlist:ArrayList<ProofImage> = arrayListOf()
    var phone_num=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_upload)
        Glide.with(this).load(R.drawable.spinner).into(loading_gif)
        loading_View.visibility= View.GONE
        val bundle=intent.extras
         phone_num= bundle?.getString("phone").toString()
        Log.i("akshay","phone"+phone_num)
        front_side_img.setOnClickListener {
            val i =  Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT

            // pass the constant to compare it
            // with the returned requestCode
            startActivityForResult(Intent.createChooser(i, "Select Picture"), GAllAYPICK);
        }

        back_side_img.setOnClickListener {
            val i =  Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(i, "Select Picture"), GAllAYPICK1)
    }
        person_photo.setOnClickListener {
            val i =  Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(i, "Select Picture"), GAllAYPICK3)
        }
        next.setOnClickListener {
            if (uploadlist.size==3){
                next.isEnabled=false
                upload_imgiees()
            }else{
                Snackbar.make(it,"Select all Images",Snackbar.LENGTH_LONG).show()
            }

        }
    }
    fun upload_imgiees(){
        CoroutineScope(Dispatchers.IO).launch{
            CoroutineScope(Dispatchers.Main).launch{
                loading_View.visibility= View.VISIBLE
            }

                async {
                    Log.i("akshay",uploadlist.size.toString())
                    val dataref=FirebaseDatabase.getInstance().getReference("Proofs")
                    for (i in 0 until uploadlist.size){
                        val StorageRef=FirebaseStorage.getInstance().getReference("Proofs").child( phone_num+i)
                        StorageRef.putFile(uploadlist[i].image).addOnSuccessListener {
                            StorageRef.downloadUrl.addOnSuccessListener {
                                val f=ProffImage(phone_num,it.toString(),uploadlist[i].type)
                                dataref.push().setValue(f).addOnSuccessListener {
                                FirebaseDatabase.getInstance().getReference("Register").child(phone_num).child(uploadlist[i].type).setValue(true)
                                }.addOnFailureListener {
                                    Toast.makeText(this@ImageUpload,it.message,Toast.LENGTH_LONG).show()
                                    // loading_View.visibility= View.GONE
                                    //recreate()
                                }
                            }

                        }
                    }
                }.await()


            CoroutineScope(Dispatchers.Main).launch {
                loading_View.visibility= View.GONE
               // val apiClient=ApiClient()

                startActivity(Intent(this@ImageUpload,Login::class.java))
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            when(requestCode){
                GAllAYPICK ->{
                   uploadlist.removeAll {
                       it.type=="Aadhaar front"
                   }
                    front_side_img.setImageURI(data?.data)
                 uploadlist.add(ProofImage(data?.data!!,"Aadhaar front"))
                }

                GAllAYPICK1->{
                    uploadlist.removeAll {
                        it.type=="Aadhaar back"
                    }
                    back_side_img.setImageURI(data?.data)
                    uploadlist.add(ProofImage(data?.data!!,"Aadhaar back"))

                }
                GAllAYPICK3 ->{
                    uploadlist.removeAll {
                        it.type=="Photo"
                    }
                    person_photo.setImageURI(data?.data)
                    uploadlist.add(ProofImage(data?.data!!,"Photo"))
                }
            }
        }

    }
}