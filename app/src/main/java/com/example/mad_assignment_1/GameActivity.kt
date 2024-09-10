package com.example.mad_assignment_1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.activity.viewModels
import com.example.mad_assignment_1.databinding.GameBinding

class GameActivity : AppCompatActivity() {
    companion object {
        const val GRID_ROWS = "com.game_activity.grid_rows"
        const val GRID_COLS = "com.game_activity.grid_cols"
        const val IS_SINGLE_PLAYER = "com.game_activity.is_single_player"
    }
    private val gameViewModel: GameInformationModel by viewModels()
    private lateinit var binding: GameBinding
    private lateinit var adapter: CellAdapter
    private val cells = mutableListOf<Cell>();
    private var numRows = 7; //hard coded for testing
    private var numCols = 6; //hard coded for testing


    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            Log.d("GameActivity", "Found Grid rows and cols")
            numRows = intent.getIntExtra(GameActivity.GRID_ROWS,  7)
            numCols = intent.getIntExtra(GameActivity.GRID_COLS, 6)
            gameViewModel.isSinglePlayer.value = intent.getBooleanExtra(GameActivity.IS_SINGLE_PLAYER, false)

        }

        super.onCreate(savedInstanceState)
        binding = GameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cells.addAll(List(numRows * numCols) {index ->

            val row  = index/numCols //logic for row of cell
            val col = index%numCols //logic for col of cell
            Cell(row, col,0) //default setup, 0 for unused
        })
        adapter = CellAdapter(cells){cell ->
            if(cell.player == 1 ||  cell.player == 2){
                return@CellAdapter //returns
            }
            print(gameViewModel.isSinglePlayer.value == false)
            if(gameViewModel.isSinglePlayer.value == false){ //checks if gamemode is single player
                if(gameViewModel.currentTurn.value == 2){
                    // current turn value 0 is AI or player 2
                    cell.player = 2
                }else{
                    cell.player = 1 // player 1's turn played
                }

            }else{ //Only 1 player vs AI
                println("MADE IT")
                if(gameViewModel.currentTurn.value == 2){
                    return@CellAdapter //return if its not your turn to click ~ shouldnt happen
                }else{
                    cell.player = 1; //Player 1's turn played
                }
            }

            gameViewModel.updateBoard(cells, numRows, numCols) //updates the turn value

        }

        binding.recyclerView.layoutManager = GridLayoutManager(this,numCols ) //sets up the grid
        binding.recyclerView.adapter = adapter

        gameViewModel.cells.observe(this){cells ->
            adapter.updateCells(cells) //updates the cell
        }

    }

}