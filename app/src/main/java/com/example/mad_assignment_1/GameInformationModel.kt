package com.example.mad_assignment_1

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras

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
class GameInformationModel(private val connectFourDao: ConnectFourDao) : ViewModel() {
    private var gameID: Long = 0;
    private var userID: Long = 0;
    private var opponentID: Long = 0;
    private var moves = mutableListOf<Int>()
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
    var playerOneColor = Color.BLUE
    var playerTwoColor = Color.RED
    var numRows = 7
    var numCols = 6

    fun updateBoard() {
        _win.value = checkForWin()
        togglePlayer()
    }

    /**
     * Save the current game state to the database
     */
    fun saveToDatabase() {
        for (cell in moves.iterator().withIndex()) {
            connectFourDao.insertCells(
                CellEntity(
                    gameID,
                    cell.value,
                    cells.value?.get(cell.value)?.player ?: 0,
                    cell.index
                )
            )
        }
        connectFourDao.updateGame(GameEntity(gameID, userID, opponentID, playerTurn.value!!, numRows, numCols))
    }

    /**
     * Remove the game from the databse
     */
    fun dropGame() {
        connectFourDao.deleteGame(gameID)
        connectFourDao.deleteGameCells(gameID)
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
    fun init(gameID: Long, isSinglePlayer: Boolean,) {
        val game = connectFourDao.getGame(gameID)
        this.gameID = gameID
        numRows = game.rows
        numCols = game.cols
        userID = game.gameUserID
        opponentID = game.opponentID
        _isSinglePlayer.value = isSinglePlayer
        reset()
        _currentTurn.value = game.playerTurn
        // Restore any cells found
        val restoredCells = connectFourDao.getGameCells(gameID)
        moves = MutableList(restoredCells.size) { _ ->
            0
        }
        for (restoredCell in restoredCells) {
            _cellsData.value
            ?.get(restoredCell.location)
            ?.player = restoredCell.player;
            moves[restoredCell.moveNo] = restoredCell.location
        }
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

    // Needed to add database to view model
    companion object {
        val Factory: ViewModelProvider.Factory = object :  ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                if (modelClass.isAssignableFrom(GameInformationModel::class.java)) {
                    return GameInformationModel((application as ConnectFourApplication).dao) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}


