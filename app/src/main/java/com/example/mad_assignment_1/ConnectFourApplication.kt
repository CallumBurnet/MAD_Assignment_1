package com.example.mad_assignment_1

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ConnectFourApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ConnectFourDatabase.getDatabase(this, applicationScope) }
    val dao by lazy {database.connectFourDao()}
}