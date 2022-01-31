package com.gates.uniongates.objects


class FrameType(var type: Int, var frame_code:String,var gate_width:String,var gate_height:String,var inner_element:String,var outer_element:String,var frameprice:Float) {

    companion object {
        const val FRAME1 = 1
        const val FRAME2 = 2
        const val FRAME3 = 3
    }

}