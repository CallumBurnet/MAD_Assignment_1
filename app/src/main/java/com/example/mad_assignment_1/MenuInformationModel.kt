package com.example.mad_assignment_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

class MenuInformationModel(private val connectFourDao: ConnectFourDao) : ViewModel() {


    private val _gridSize = MutableLiveData<GridSize>()
    val gridSize: LiveData<GridSize> get() = _gridSize

    private val _isSinglePlayer = MutableLiveData<Boolean>()
    val isSinglePlayer: LiveData<Boolean> get() = _isSinglePlayer
    private val _users: LiveData<List<UserEntity>> = connectFourDao.getUsers() // LiveData<List<UserEntity>>
    private val imageResourceMap = mapOf(
        1 to R.drawable.avatar1,
        2 to R.drawable.avatar,
        3 to R.drawable.avatar2,
        4 to R.drawable.avatar3
    )

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
    fun getImageResource(profilePicId: Int): Int? {
        return imageResourceMap[profilePicId]
    }
    fun setGameMode(isSinglePlayer: Boolean){
        _isSinglePlayer.value = isSinglePlayer
    }
    //Primary user
    private val _activePrimaryUser = MutableLiveData<UserEntity>()
    val activePrimaryUser: LiveData<UserEntity> get() = _activePrimaryUser
    //Second user
    private val _activeSecondaryUser = MutableLiveData<UserEntity>()
    val activeSecondaryUser: LiveData<UserEntity> get() = _activeSecondaryUser

    //Sets
    fun setPrimaryUser(user: UserEntity){
        _activePrimaryUser.value = user
    }
    fun setSecondaryUser(user: UserEntity){
        _activeSecondaryUser.value = user
    }
    fun getPrimaryUser(): UserEntity? {
        return _activePrimaryUser.value
    }
    fun getSecondaryUser(): UserEntity? {
        return _activeSecondaryUser.value
    }

    fun getUsers(): LiveData<List<UserEntity>> {
        return _users
    }
    fun addUser(user: UserProfile) {
        viewModelScope.launch {
            // Create the UserEntity with null for the auto-generated ID
            val userEntity = UserEntity(
                userID = 0, // This can be ignored, but for clarity, you can keep it as 0
                name = user.userName,
                profilePic = user.imageResId
            )

            // Insert the user into the database
            connectFourDao.insertUser(userEntity)

            // Update the LiveData list on the main thread
            val updatedUsers = connectFourDao.getUsers()
        }
    }

    enum class GridSize {
        SMALL,
        STANDARD,
        LARGE
    }
    private val userImageMap = HashMap<Int, Int>()

    fun allocateImageResource(userProfilePic: Int, imageResId: Int) {
        userImageMap[userProfilePic] = imageResId
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