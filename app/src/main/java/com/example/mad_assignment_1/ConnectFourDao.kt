package com.example.mad_assignment_1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ConnectFourDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: GameEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCells(vararg cells: CellEntity)

    @Update(entity = GameEntity::class)
    fun updateGame(game: GameEntity)

    @Query("DELETE FROM user_table")
    fun deleteAllUser()

    @Query("DELETE FROM game_table")
    fun deleteAllGame()

    @Query("DELETE FROM cells_table")
    fun deleteAllCells()

    @Query("SELECT * FROM user_table")
    fun getUsers(): List<UserEntity>

    @Query("SELECT * FROM user_table WHERE userID=:userID")
    fun getUser(userID: Int): UserEntity?

    @Query("SELECT * FROM game_table WHERE gameID=:gameID")
    fun getGame(gameID: Long): GameEntity

    @Query("DELETE FROM cells_table WHERE gameID=:gameID")
    fun deleteGameCells(gameID: Long)

    @Query("DELETE FROM game_table WHERE gameID=:gameID")
    fun deleteGame(gameID: Long)

    @Query("SELECT * FROM game_table WHERE opponentID=:opponent AND gameUserID=:user")
    fun getGamesWithUsers(user: Int, opponent: Int): List<GameEntity>

    @Query("SELECT * FROM cells_table WHERE gameID = :gameID")
    fun getGameCells(gameID: Long): List<CellEntity>
}