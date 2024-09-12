package com.example.mad_assignment_1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameInformationModel : ViewModel(){
    val cells = MutableLiveData<List<Cell>>()
    val currentTurn = MutableLiveData<Int>()
    val isSinglePlayer = MutableLiveData<Boolean>()
    val player = currentTurn.value ?: 1

    fun updateBoard(cells: List<Cell>, numRows: Int, numCols: Int){
        if(checkForWin(cells, numRows, numCols)){
            print("Win")
        }
        togglePlayer()
    }
    private fun togglePlayer(){
        if(currentTurn.value == 2){
            currentTurn.value = 1
        }else{
            currentTurn.value = 2
        }
        println(currentTurn.value)
    }
    private fun checkForWin(cells: List<Cell>, numRows: Int, numCols: Int): Boolean {
        // Horizontal check
        for (row in 0 until numRows) {
            for (col in 0 until numCols - 3) {
                if (cells[row * numCols + col].player == player &&
                    cells[row * numCols + col + 1].player == player &&
                    cells[row * numCols + col + 2].player == player &&
                    cells[row * numCols + col + 3].player == player
                ) {
                    return true
                }
            }
        }
        // Vertical check
        for (col in 0 until numCols) {
            for (row in 0 until numRows - 3) {
                if (cells[row * numCols + col].player == player &&
                    cells[(row + 1) * numCols + col].player == player &&
                    cells[(row + 2) * numCols + col].player == player &&
                    cells[(row + 3) * numCols + col].player == player
                ) {
                    return true
                }
            }
        }
        // Diagonal  + axis
        for (row in 0 until numRows - 3) {
            for (col in 0 until numCols - 3) {
                if (cells[row * numCols + col].player == player &&
                    cells[(row + 1) * numCols + col + 1].player == player &&
                    cells[(row + 2) * numCols + col + 2].player == player &&
                    cells[(row + 3) * numCols + col + 3].player == player
                ) {
                    return true
                }
            }
        }
        // Diagonal - axis
        for (row in 3 until numRows) {
            for (col in 0 until numCols - 3) {
                if (cells[row * numCols + col].player == player &&
                    cells[(row - 1) * numCols + col + 1].player == player &&
                    cells[(row - 2) * numCols + col + 2].player == player &&
                    cells[(row - 3) * numCols + col + 3].player == player
                ) {
                    return true
                }
            }
        }
        return false
    }

}


