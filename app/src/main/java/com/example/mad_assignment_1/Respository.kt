package com.example.mad_assignment_1

import androidx.annotation.WorkerThread

class Respository(private val connectFourDao: ConnectFourDao) {
    val users = connectFourDao.getUsers()

    @WorkerThread
    suspend fun insert(user: UserEntity) {
        connectFourDao.insertUser(user)
    }
}