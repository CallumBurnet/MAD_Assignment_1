package com.example.mad_assignment_1

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userID: Long,
    val name: String,
    val profilePic: Int,
)
@Entity(tableName = "game_table",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userID"],
            childColumns = ["opponentID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userID"],
            childColumns = ["gameUserID"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["gameUserID", "opponentID"])
    ]
)
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val gameID: Long,
    val gameUserID: Long,
    val opponentID: Long,
    val playerTurn: Int,
    val rows: Int,
    val cols: Int,
)

@Entity(tableName = "cells_table", primaryKeys = ["gameID", "row", "col"])
data class CellEntity(
    val gameID: Long,
    val row: Int,
    val col: Int,
    val player: Int,
)

//data class GamesWithUser(
//    @Embedded
//    val user: UserEntity,
//    @Relation(
//        parentColumn = "userID",
//        entityColumn = "gameUserID"
//    )
//    val games: List<GameEntity>
//)
//
//data class GamesWithUserAndOpponents(
//    @Embedded
//    val opponent: UserEntity,
//    @Relation(
//        entity = UserEntity::class,
//        parentColumn = "userID",
//        entityColumn = "opponentID"
//    )
//    val games: List<GamesWithUser>
//)