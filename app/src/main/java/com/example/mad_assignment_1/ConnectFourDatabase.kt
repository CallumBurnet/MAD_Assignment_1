package com.example.mad_assignment_1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [UserEntity::class, GameEntity::class, CellEntity::class], version = 1, exportSchema = false)
abstract class ConnectFourDatabase : RoomDatabase() {
    abstract fun connectFourDao(): ConnectFourDao

    companion object {
        @Volatile
        private var INSTANCE: ConnectFourDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ConnectFourDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =  Room.databaseBuilder(
                    context.applicationContext,
                    ConnectFourDatabase::class.java,
                    "connect_four_database"
                ).addCallback(ConnectFourDatabaseCallback(scope))
                 .allowMainThreadQueries()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ConnectFourDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val connectFourDao = database.connectFourDao()

                    // Ensure default users exist
                    if (connectFourDao.getUser(1)?.name != "Player 1")  {
                        connectFourDao.insertUser(UserEntity(1, "Player 1", 0))
                    }
                    if (connectFourDao.getUser(2)?.name != "Player 2")  {
                        connectFourDao.insertUser(UserEntity(2, "Player 2", 1))
                    }
                    if (connectFourDao.getUser(3)?.name != "Computer") {
                        connectFourDao.insertUser(UserEntity(3, "Computer", 1))
                    }
                }
            }
        }
    }
}
