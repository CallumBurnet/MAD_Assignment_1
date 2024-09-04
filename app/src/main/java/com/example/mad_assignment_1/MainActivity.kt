package com.example.mad_assignment_1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mad_assignment_1.databinding.ActivityMainBinding
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val gameViewModel: GameInformationModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CellAdapter
    private val cells = mutableListOf<Cell>();
    var numRows = 6; //hard coded for testing
    var numCols = 7; //hard coded for testing


    override fun onCreate(savedInstanceState: Bundle?) {
        gameViewModel.isSinglePlayer.value = false //FOR TESTING PURPOSES

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cells.addAll(List(42){index ->

            val row  = index/numCols //logic for row of cell
            val col = index%numCols //logic for col of cell
            Cell(row, col,0) //default setup, 0 for unused
        })
        adapter = CellAdapter(cells){cell ->
            if(cell.player == 1 ||  cell.player == 2){
                return@CellAdapter //returns
            }
            if(gameViewModel.isSinglePlayer.value == false){ //checks if gamemode is single player
                if(gameViewModel.currentTurn.value == 0){
                    // current turn value 0 is AI or player 2
                    cell.player = 2
                }else{
                    cell.player = 1 // player 1's turn played
                }

            }else{ //Only 1 player vs AI
                if(gameViewModel.currentTurn.value == 0){
                    return@CellAdapter //return if its not your turn to click ~ shouldnt happen
                }else{
                    cell.player = 1; //Player 1's turn played
                }
            }

            gameViewModel.updateBoard() //updates the turn value

        }
        binding.recyclerView.layoutManager = GridLayoutManager(this,numCols ) //sets up the grid
        binding.recyclerView.adapter = adapter

        gameViewModel.cells.observe(this){cells ->
            adapter.updateCells(cells) //updates the cell
        }

    }

}