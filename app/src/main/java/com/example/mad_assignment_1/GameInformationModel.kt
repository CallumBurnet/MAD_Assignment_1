package com.example.mad_assignment_1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Holds the state for the game
 *
 * @property cells List of cells in the board
 * @property _cellsData Underlying mutable cell data
 * @property _currentTurn What the current turn is
 * @property isSinglePlayer Whether the game is single player or not
 * @property numCols Number of columns
 * @property numRows Number of rows
 */
class GameInformationModel() : ViewModel() {
    private val moves = mutableListOf<Int>()
    private val _cellsData = MutableLiveData<List<Cell>>()
    private val _currentTurn = MutableLiveData<Int>()
    private val _win = MutableLiveData<Boolean>()
    private val _isSinglePlayer = MutableLiveData<Boolean>();
    val cells: LiveData<List<Cell>>
        get() = _cellsData
    val playerTurn: LiveData<Int>
        get() = _currentTurn
    val win: LiveData<Boolean>
        get() = _win
    val isSinglePlayer: LiveData<Boolean>
        get() = _isSinglePlayer
    var numRows = 7
    var numCols = 6

    fun updateBoard() {
        _win.value = checkForWin()
        togglePlayer()
    }

    /** Makes sure the disc drops to the lowest level
     * @return true means the bottom is reached, false means falling
     */
    fun dropDisc(row: Int, col: Int): Boolean {
        // Check whether we are empty
        if (cells.value?.get(row * numCols + col)?.player != 0) {
            return true
        } else if (row == numRows - 1) {
            // If we are at the bottom and its empty then we wack it in
            cells.value?.get(row * numCols + col)?.player = _currentTurn.value?: 1
            moves.add(row * numCols + col)
            // Force an update
            _cellsData.value = _cellsData.value;
            return false
        } else {
            if (dropDisc(row + 1, col)) {
                // If the disk below us is full then we can place the disc here
                cells.value?.get(row * numCols + col)?.player = _currentTurn.value?: 1
                moves.add(row * numCols + col)
                // Force an update
                _cellsData.value = _cellsData.value
            }
            return false
        }
    }

   /** Undo a move
    * @return Returns true if succeeded, false if failed
    */
    fun undo(): Boolean {
        if (moves.isEmpty()) {
            return false
        } else {
            _cellsData.value?.get(moves.removeLast())?.player = 0;
            _win.value = false
            if (_currentTurn.value == 1) {
                _currentTurn.value = 2
            } else {
                _currentTurn.value = 1
            }
            // Force an update
            _cellsData.value = _cellsData.value
            return true
        }
    }

    /** Resets the boards cells
     */
    fun reset() {
        _cellsData.value = List(numRows * numCols) { index ->
            val row = index / numCols //logic for row of cell
            val col = index % numCols //logic for col of cell
            Cell(row, col, 0) //default setup, 0 for unused
        }
        _win.value = false
        _currentTurn.value = 1
    }

    /**
     * Initialise the view model
     */
    fun init(rows: Int, cols: Int, isSinglePlayer: Boolean,) {
        numRows = rows
        numCols = cols
        _isSinglePlayer.value = isSinglePlayer
        reset()
    }

    private fun togglePlayer(){
        if(_currentTurn.value == 2){
            _currentTurn.value = 1
        }else{
            _currentTurn.value = 2
        }
        Log.d("BOARD", "Current turn: ${_currentTurn.value}")
    }

    private fun checkForWin(): Boolean {
        // Horizontal check
        for (row in 0 until numRows) {
            for (col in 0 until numCols - 3) {
                if (cells.value?.get(row * numCols + col)?.player == _currentTurn.value &&
                    cells.value?.get(row * numCols + col + 1)?.player == _currentTurn.value &&
                    cells.value?.get(row * numCols + col + 2)?.player == _currentTurn.value &&
                    cells.value?.get(row * numCols + col + 3)?.player == _currentTurn.value
                ) {
                    return true
                }
            }
        }
        // Vertical check
        for (col in 0 until numCols) {
            for (row in 0 until numRows - 3) {
                if (cells.value?.get(row * numCols + col)?.player == _currentTurn.value &&
                    cells.value?.get((row + 1) * numCols + col)?.player == _currentTurn.value &&
                    cells.value?.get((row + 2) * numCols + col)?.player == _currentTurn.value &&
                    cells.value?.get((row + 3) * numCols + col)?.player == _currentTurn.value
                ) {
                    return true
                }
            }
        }
        // Diagonal  + axis
        for (row in 0 until numRows - 3) {
            for (col in 0 until numCols - 3) {
                if (cells.value?.get(row * numCols + col)?.player == _currentTurn.value &&
                    cells.value?.get((row + 1) * numCols + col + 1)?.player == _currentTurn.value &&
                    cells.value?.get((row + 2) * numCols + col + 2)?.player == _currentTurn.value &&
                    cells.value?.get((row + 3) * numCols + col + 3)?.player == _currentTurn.value
                ) {
                    return true
                }
            }
        }
        // Diagonal - axis
        for (row in 3 until numRows) {
            for (col in 0 until numCols - 3) {
                if (cells.value?.get(row * numCols + col)?.player == _currentTurn.value &&
                    cells.value?.get((row - 1) * numCols + col + 1)?.player == _currentTurn.value &&
                    cells.value?.get((row - 2) * numCols + col + 2)?.player == _currentTurn.value &&
                    cells.value?.get((row - 3) * numCols + col + 3)?.player == _currentTurn.value
                ) {
                    return true
                }
            }
        }
        return false
    }

}


