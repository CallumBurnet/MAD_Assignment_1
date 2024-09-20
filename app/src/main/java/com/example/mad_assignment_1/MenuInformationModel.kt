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
    private val users = MutableLiveData<MutableList<UserProfile>>(mutableListOf())
    //Primary user
    private val _activePrimaryUser = MutableLiveData<UserProfile>()
    val activePrimaryUser: LiveData<UserProfile> get() = _activePrimaryUser
    //Second user
    private val _activeSecondaryUser = MutableLiveData<UserProfile>()
    val activeSecondaryUser: LiveData<UserProfile> get() = _activeSecondaryUser

    //Sets
    fun setPrimaryUser(user: UserProfile){
        _activePrimaryUser.value = user
    }
    fun setSecondaryUser(user: UserProfile){
        _activeSecondaryUser.value = user
    }
    fun getPrimaryUser(): UserProfile? {
        return _activePrimaryUser.value
    }
    fun getSecondaryUser(): UserProfile? {
        return _activeSecondaryUser.value
    }

    fun getUsers(): LiveData<MutableList<UserProfile>>{
        return users
    }
    fun addUser(user: UserProfile){
        users.value?.add(user)
        users.value = users.value
    }


    enum class GridSize {
        SMALL,
        STANDARD,
        LARGE
    }
}