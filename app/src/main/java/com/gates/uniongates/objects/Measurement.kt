package com.gates.uniongates.objects

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Measurement:Serializable{
    var cm:String=""
    var feet:String=""
    var inch:String=""
    var m:String=""

    constructor(dataSnap:HashMap<String,Any>){
        val h:HashMap<String,Any> =dataSnap
        cm=h["cm"] as String
        feet=h["feet"] as String
        inch=h["inch"] as String
        m=h["m"] as String
    }
    constructor(cm: String, feet: String, inch: String, m: String) {
        this.cm = cm
        this.feet = feet
        this.inch = inch
        this.m = m
    }
}