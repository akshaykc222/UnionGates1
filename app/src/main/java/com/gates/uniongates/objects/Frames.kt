package com.gates.uniongates.objects

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Frames:Serializable {
    var frameCode =""
    var frameCost =""
    lateinit var height:Measurement
    lateinit var width:Measurement
    var frameInner =""
    var frameOuter =""

    var frameId=""
    var frame_img =""
    var is1200 = false
    var is300= false
    var is500 = true
    var panels=""
    constructor(snapshot:DataSnapshot,image:String){
        val h:HashMap<String,Any> =snapshot.value as HashMap<String, Any>
        frameCode=h["frameCode"] as String
        frameCost=h["cost"] as String
       height= Measurement(h["height"] as HashMap<String, Any>)
        width= Measurement(h["width"] as HashMap<String, Any>)
      //  frame_cost2=h["frame_cost2"] as String
        frameInner=h["frameInner"] as String
        frameOuter=h["frameOuter"] as String
       // frame_desc3=h["frame_desc3"] as String
        frameId=h["id"] as String
        frame_img=image
        is1200=h["is1200"] as Boolean
        is300=h["is300"] as Boolean
        is500=h["is500"] as Boolean
        panels=h["panels"] as String



    }

    constructor(
        frame_code: String,
        frame_cost1: String,
        frame_desc1: String,
        frame_desc2: String,
        frame_id: String,
        frame_img: String,
        is1200: Boolean,
        is300: Boolean,
        is500: Boolean,
        panels: String,
        height:Measurement,
        width:Measurement
    ) {
        this.frameCode = frame_code
        this.frameCost = frame_cost1
        //this.frame_cost2 = frame_cost2
        this.frameInner = frame_desc1
        this.frameOuter = frame_desc2
       // this.frame_desc3 = frame_desc3
        this.frameId = frame_id
        this.frame_img = frame_img
        this.is1200 = is1200
        this.is300 = is300
        this.is500 = is500
        this.panels = panels
        this.height=height
        this.width=width
    }
}