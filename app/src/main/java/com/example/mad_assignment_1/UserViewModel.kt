package com.example.mad_assignment_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val users = MutableLiveData<MutableList<UserProfile>>(mutableListOf())
    fun getUsers(): LiveData<MutableList<UserProfile>>{
        return users
    }
    fun addUser(user: UserProfile){
       users.value?.add(user)
        users.value = users.value
    }

}