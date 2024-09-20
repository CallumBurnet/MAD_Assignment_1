package com.example.mad_assignment_1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

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
    private var gameID: Long = 0
    private var primaryUserID: Long = 0
    private var secondaryUserID: Long = 0
    private var moves = mutableListOf<Int>()
    private val _cellsData = MutableLiveData<List<Cell>>()
    private val _currentTurn = MutableLiveData<Int>()
    private val _win = MutableLiveData<Boolean>()
    private val _isSinglePlayer = MutableLiveData<Boolean>()

    val cells: LiveData<List<Cell>> get() = _cellsData
    val playerTurn: LiveData<Int> get() = _currentTurn
    val win: LiveData<Boolean> get() = _win
    val isSinglePlayer: LiveData<Boolean> get() = _isSinglePlayer
    var numRows = 7
    var numCols = 6

    fun updateBoard() {
        _win.value = checkForWin()
        togglePlayer()
    }

    fun saveToDatabase() {
        viewModelScope.launch {
            moves.forEachIndexed { index, move ->
                connectFourDao.insertCells(
                    CellEntity(gameID, move, cells.value?.get(move)?.player ?: 0, index)
                )
            }
            connectFourDao.updateGame(
                GameEntity(gameID, primaryUserID, secondaryUserID, playerTurn.value ?: 1, numRows, numCols)
            )
        }
    }

    fun dropGame() {
        viewModelScope.launch {
            connectFourDao.deleteGame(gameID)
            connectFourDao.deleteGameCells(gameID)
        }
    }

    fun dropDisc(row: Int, col: Int): Boolean {
        val boardCells = _cellsData.value ?: return false
        val cell = boardCells[row * numCols + col]
        if (cell.player != 0) return true
        if (row == numRows - 1 || dropDisc(row + 1, col)) {
            val updatedCells = boardCells.toMutableList()
            updatedCells[row * numCols + col] = cell.copy(player = _currentTurn.value ?: 1)
            moves.add(row * numCols + col)
            _cellsData.value = updatedCells
            return false
        }
        return false
    }

    fun undo(): Boolean {
        if (moves.isEmpty()) return false
        val lastMove = moves.removeLast()
        val updatedCells = _cellsData.value?.toMutableList() ?: return false
        updatedCells[lastMove] = updatedCells[lastMove].copy(player = 0)
        _cellsData.value = updatedCells
        togglePlayer()
        _win.value = false
        return true
    }

    fun reset() {
        _cellsData.value = List(numRows * numCols) { index ->
            val row = index / numCols
            val col = index % numCols
            Cell(row, col, 0)
        }
        _currentTurn.value = 1
        _win.value = false
    }

    fun init(gameID: Long, isSinglePlayer: Boolean) {
        viewModelScope.launch {
            val game = connectFourDao.getGame(gameID) ?: return@launch
            this@GameInformationModel.gameID = gameID
            numRows = game.rows
            numCols = game.cols
            primaryUserID = game.gameUserID
            secondaryUserID = game.opponentID
            _isSinglePlayer.value = isSinglePlayer
            _currentTurn.value = game.playerTurn
            reset()

            val restoredCells = connectFourDao.getGameCells(gameID)
            moves = MutableList(restoredCells.size) { 0 }
            val updatedCells = _cellsData.value?.toMutableList() ?: return@launch

            for (restoredCell in restoredCells) {
                updatedCells[restoredCell.location] = updatedCells[restoredCell.location].copy(player = restoredCell.player)
                moves[restoredCell.moveNo] = restoredCell.location
            }
            _cellsData.value = updatedCells
        }
    }

    fun togglePlayer() {
        _currentTurn.value = if (_currentTurn.value == 1) 2 else 1
        Log.d("BOARD", "Current turn: ${_currentTurn.value}")
    }

    private fun checkForWin(): Boolean {
        val boardCells = cells.value ?: return false
        val currentPlayer = _currentTurn.value ?: return false

        // Horizontal check
        for (row in 0 until numRows) {
            for (col in 0 until numCols - 3) {
                if (boardCells[row * numCols + col].player == currentPlayer &&
                    boardCells[row * numCols + col + 1].player == currentPlayer &&
                    boardCells[row * numCols + col + 2].player == currentPlayer &&
                    boardCells[row * numCols + col + 3].player == currentPlayer) {
                    return true
                }
            }
        }

        // Vertical check
        for (col in 0 until numCols) {
            for (row in 0 until numRows - 3) {
                if (boardCells[row * numCols + col].player == currentPlayer &&
                    boardCells[(row + 1) * numCols + col].player == currentPlayer &&
                    boardCells[(row + 2) * numCols + col].player == currentPlayer &&
                    boardCells[(row + 3) * numCols + col].player == currentPlayer) {
                    return true
                }
            }
        }

        // Diagonal checks (both directions)
        // + Diagonal
        for (row in 0 until numRows - 3) {
            for (col in 0 until numCols - 3) {
                if (boardCells[row * numCols + col].player == currentPlayer &&
                    boardCells[(row + 1) * numCols + col + 1].player == currentPlayer &&
                    boardCells[(row + 2) * numCols + col + 2].player == currentPlayer &&
                    boardCells[(row + 3) * numCols + col + 3].player == currentPlayer) {
                    return true
                }
            }
        }

        // - Diagonal
        for (row in 3 until numRows) {
            for (col in 0 until numCols - 3) {
                if (boardCells[row * numCols + col].player == currentPlayer &&
                    boardCells[(row - 1) * numCols + col + 1].player == currentPlayer &&
                    boardCells[(row - 2) * numCols + col + 2].player == currentPlayer &&
                    boardCells[(row - 3) * numCols + col + 3].player == currentPlayer) {
                    return true
                }
            }
        }
        return false
    }

    fun updateUserStatistics(winnerID: Long, loserID: Long) {
        viewModelScope.launch {
            val winner = connectFourDao.getUser(winnerID)
            val loser = connectFourDao.getUser(loserID)

            if (winner != null && loser != null) {
                winner.wins += 1
                loser.losses += 1
                connectFourDao.updateUser(winner)
                connectFourDao.updateUser(loser)
            }
        }
    }

    fun getPrimaryUserId(): Long = primaryUserID
    fun getSecondaryUserId(): Long = secondaryUserID

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
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
