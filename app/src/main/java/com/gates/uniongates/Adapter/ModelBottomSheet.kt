package com.gates.uniongates.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gates.uniongates.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModelBottomSheet: BottomSheetDialogFragment() {
    var viewClicked=false;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
    fun isViewClicked():Boolean{
        return isViewClicked()
    }
}