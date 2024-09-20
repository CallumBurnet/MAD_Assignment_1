package com.example.mad_assignment_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras

class MenuInformationModel(private val connectFourDao: ConnectFourDao) : ViewModel() {
    private val _gridSize = MutableLiveData<GridSize>()
    val gridSize: LiveData<GridSize> get() = _gridSize

    private val _isSinglePlayer = MutableLiveData<Boolean>()
    val isSinglePlayer: LiveData<Boolean> get() = _isSinglePlayer

    fun setGridSize(gridSize: GridSize) {
        _gridSize.value = gridSize
    }

    fun forceUpdate() {
        _isSinglePlayer.value = _isSinglePlayer.value
    }

    fun checkForGames(userID: Int, opponentID: Int): List<GameEntity> {
        return connectFourDao.getGamesWithUsers(userID, opponentID)
    }

    fun newGame(rows: Int, cols: Int, userID: Long, opponentID: Long): Long {
        return connectFourDao.insertGame(GameEntity(0, userID, opponentID, 1, rows, cols))
    }

    fun setGameMode(isSinglePlayer: Boolean){
        _isSinglePlayer.value = isSinglePlayer
    }

    enum class GridSize {
        SMALL,
        STANDARD,
        LARGE
    }

    // Needed to add database to view model
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                if (modelClass.isAssignableFrom(MenuInformationModel::class.java)) {
                    return MenuInformationModel((application as ConnectFourApplication).dao) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}