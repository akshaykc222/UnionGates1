package com.gates.uniongates.objects

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Gates:Serializable {
    var gateId:String? =""
    var gateName:String?=""
    var gateCost:String?=""
    var gateDesc:String?=""
    var gateImage:String?=""

    constructor(gateId: String, gateName: String, gateCost: String, gateDesc: String, gateImage: String) {
        this.gateId = gateId
        this.gateName = gateName
        this.gateCost = gateCost
        this.gateDesc = gateDesc
        this.gateImage = gateImage
    }
    constructor(dataSnapshot: DataSnapshot){
        val h:HashMap<String,Any> =dataSnapshot.value as HashMap<String, Any>
        gateId=h["gate_code"] as String?
        gateName=h["gate_name"] as String?
        gateCost= (h["gate_cost"] ).toString()
        gateDesc=h["gate_desc"] as String?
        gateImage=h["gate_img"] as String?

    }
}