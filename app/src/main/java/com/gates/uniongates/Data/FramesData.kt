package com.gates.uniongates.Data

import com.gates.uniongates.objects.Measurement

data class FramesData (
    var frameCode:String,
    var frameCost :String,
    var height: Measurement,
    var width: Measurement,
    var frameInner :String,
    var frameOuter :String,

    var frameId:String,
    var frame_img :String,
    var is1200 :Boolean,
    var is300:Boolean,
    var is500:Boolean,
    var panels:String

        )