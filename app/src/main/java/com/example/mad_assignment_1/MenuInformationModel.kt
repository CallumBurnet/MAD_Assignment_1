package com.example.mad_assignment_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuInformationModel : ViewModel() {
    private val _gridSize = MutableLiveData<GridSize>()
    val gridSize: LiveData<GridSize> get() = _gridSize

    fun setGridSize(gridSize: GridSize) {
        _gridSize.value = gridSize
    }

    enum class GridSize {
        SMALL,
        STANDARD,
        LARGE
    }
}