package com.example.mad_assignment_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment_1.MenuInformationModel.GridSize

class UserViewModel : ViewModel() {
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

    fun getUsers(): LiveData<MutableList<UserProfile>>{
        return users
    }
    fun addUser(user: UserProfile){
       users.value?.add(user)
        users.value = users.value
    }

}