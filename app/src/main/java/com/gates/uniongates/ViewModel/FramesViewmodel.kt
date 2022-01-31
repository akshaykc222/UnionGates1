package com.gates.uniongates.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gates.uniongates.Data.FramesData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FramesViewModel @Inject constructor():ViewModel(){
    private val FramesList= MutableLiveData<List<FramesData>>()
    val frames:LiveData<List<FramesData>> = FramesList

    init {

        viewModelScope.launch {
            FramesList
        }
    }
}