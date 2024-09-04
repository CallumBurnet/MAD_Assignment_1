package com.example.mad_assignment_1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameInformationModel : ViewModel(){
    val cells = MutableLiveData<List<Cell>>()
    val currentTurn = MutableLiveData<Int>()
    val isSinglePlayer = MutableLiveData<Boolean>()

    fun updateBoard(){
        togglePlayer()
    }
    private fun togglePlayer(){
        if(currentTurn.value == 0){
            currentTurn.value = 1
        }else{
            currentTurn.value = 0
        }
        println(currentTurn.value)
    }

}