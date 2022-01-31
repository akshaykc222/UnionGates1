package com.gates.uniongates.objects

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

 class Panel :Serializable{
  var panel_cost:String?=""
  var panelCode:String?=""
 // var panel_desc1:String?=""
  var panel_desc2:String?=""
  var panel_img:String?= ""
 var panelHeight:String=""
  var panelWidth:String?= ""
  var panel_type:String?=""
     var isFree:Boolean?=false;
 constructor(datasnap:DataSnapshot,image:String){
  val data:HashMap<String,Any> =datasnap.value as HashMap<String, Any>
  panel_cost=data["panelPrice"] as String?
  panelCode=data["panelCode"] as String?
  panelHeight=data["height"] as String
  panel_desc2=data["desc2"] as String?
  panel_img=image
  panelWidth=data["width"] as String?
  panel_type=data["type"] as String?
     isFree=data["isFree"] as Boolean?
 }

     constructor(
         panel_cost: String?,
         panelCode: String?,
         panel_desc2: String?,
         panel_img: String?,
         panelHeight: String,
         panelWidth: String?,
         panel_type: String?,
         isFree: Boolean?
     ) {
         this.panel_cost = panel_cost
         this.panelCode = panelCode
         this.panel_desc2 = panel_desc2
         this.panel_img = panel_img
         this.panelHeight = panelHeight
         this.panelWidth = panelWidth
         this.panel_type = panel_type
         this.isFree = isFree
     }


 }

