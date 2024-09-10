package com.example.mad_assignment_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuInformationModel : ViewModel() {
    private val _gridSize = MutableLiveData<GridSize>()
    val gridSize: LiveData<GridSize> get() = _gridSize

    private val _isSinglePlayer = MutableLiveData<Boolean>()
    val isSinglePlayer: LiveData<Boolean> get() = _isSinglePlayer

    fun setGridSize(gridSize: GridSize) {
        _gridSize.value = gridSize
    }
    fun setGameMode(isSinglePlayer: Boolean){
        _isSinglePlayer.value = isSinglePlayer
    }


    enum class GridSize {
        SMALL,
        STANDARD,
        LARGE
    }
}