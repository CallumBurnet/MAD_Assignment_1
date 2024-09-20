package com.example.mad_assignment_1

/** Represents a cell on the board.
 * @property player 0 = unassigned, player 1 and 2 are their respective players
 */
//Data class for the individual disc slots
data class Cell(val row: Int, val col: Int, var player: Int)