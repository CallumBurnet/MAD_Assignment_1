package com.example.mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.activity.viewModels
import com.example.mad_assignment_1.databinding.GameBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    companion object {
        const val GAME_ID = "com.game_activity.gameID"
        const val IS_SINGLE_PLAYER = "com.game_activity.is_single_player"
    }
    private val gameViewModel: GameInformationModel by viewModels {
        GameInformationModel.Factory
    }
    private lateinit var binding: GameBinding
    private lateinit var adapter: CellAdapter
    private val cells = mutableListOf<Cell>()
    private var numRows = 7; //hard coded for testing
    private var numCols = 6; //hard coded for testing


    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            Log.d("GameActivity", "Found Grid rows and cols")
            gameViewModel.init(
                intent.getLongExtra(GAME_ID, 0),
                intent.getBooleanExtra(GameActivity.IS_SINGLE_PLAYER, false)
            )
        }

        super.onCreate(savedInstanceState)
        binding = GameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CellAdapter(gameViewModel)
        binding.undoButton.setOnClickListener { view  ->
            if (!gameViewModel.undo()) {
                Snackbar.make(view, "No moves to undo", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.resetButton.setOnClickListener { _ ->
            gameViewModel.reset()
        }
        binding.exitControlsButton.setOnClickListener { _ ->
            showControls()
        }
        binding.cancelExitButton.setOnClickListener { _ ->
            hideControls()
        }
        binding.pauseButton.setOnClickListener { _ ->
            hideControls()
            gameViewModel.saveToDatabase()
            setResult(RESULT_OK)
            finish()
        }
        binding.exitButton.setOnClickListener { _ ->
            hideControls()
            // Remove the game from the database as we don't want it saved
            gameViewModel.dropGame()
            setResult(RESULT_OK)
            finish()
        }
        gameViewModel.playerTurn.observe(this) { turn ->
            if (gameViewModel.win.value != true) {
                binding.gameState.text = "Player ${turn} turn"
            }
        }
        gameViewModel.win.observe(this)  { win ->
            if (win) {
                if (gameViewModel.isSinglePlayer.value == true && gameViewModel.playerTurn.value == 2) {
                    binding.gameState.text = "Computer has won!!"
                } else {
                    binding.gameState.text = "Player ${gameViewModel.playerTurn.value} has won!!"
                }
            }
        }
        if (gameViewModel.isSinglePlayer.value == true) {
            gameViewModel.playerTurn.observe(this) { turn ->
                if (turn == 2) {
                    do {
                        val dropPoint = Random.nextInt(0, gameViewModel.numCols - 1)
                    } while (gameViewModel.dropDisc(0, dropPoint))
                }
            }
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, gameViewModel.numCols) //sets up the grid
        binding.recyclerView.adapter = adapter
    }

    private fun showControls() {
        binding.cancelExitButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.VISIBLE
        binding.exitButton.visibility = View.VISIBLE
        binding.exitControlsButton.visibility = View.GONE
    }

    private fun hideControls() {
        binding.cancelExitButton.visibility = View.GONE
        binding.pauseButton.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
        binding.exitControlsButton.visibility = View.VISIBLE
    }
}