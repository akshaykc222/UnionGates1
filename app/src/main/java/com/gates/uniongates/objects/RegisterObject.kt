package com.gates.uniongates.objects

import com.google.firebase.database.DataSnapshot

class RegisterObject{
    var name:String? =""
    var address:String?=""
    var phone:String?=""
    var refered:String?=""
    var adhar:String?=""
    var IsApproved:Boolean?=false
    var account_num:String?=""
    var ifscCode:String?=""
    constructor(datasnap:DataSnapshot){
        val data:HashMap<String,Any> =datasnap.value as HashMap<String, Any>
        name=data["name"] as String
        address=data["address"] as String
        phone=data["phone"] as String
        refered=data["refered"] as String
        adhar=data["adhar"] as String
        IsApproved=data["IsApproved"] as Boolean
        account_num=data["account_num"] as String
        ifscCode=data["ifscCode"] as String
    }
    constructor(name: String, address: String, phone: String, refered: String, adhar: String, IsApproved: Boolean, account_num: String,ifscCode:String) {
        this.name = name
        this.address = address
        this.phone = phone
        this.refered = refered
        this.adhar = adhar
        this.IsApproved = IsApproved
        this.account_num = account_num
        this.ifscCode=ifscCode
    }
}