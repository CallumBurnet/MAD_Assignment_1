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
        const val GRID_ROWS = "com.game_activity.grid_rows"
        const val GRID_COLS = "com.game_activity.grid_cols"
        const val IS_SINGLE_PLAYER = "com.game_activity.is_single_player"
    }
    private val gameViewModel: GameInformationModel by viewModels()
    private lateinit var binding: GameBinding
    private lateinit var adapter: CellAdapter
    private val cells = mutableListOf<Cell>();


    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            Log.d("GameActivity", "Found Grid rows and cols")
            gameViewModel.init(
                intent.getIntExtra(GameActivity.GRID_ROWS, 7),
                intent.getIntExtra(GameActivity.GRID_COLS, 6),
                intent.getBooleanExtra(GameActivity.IS_SINGLE_PLAYER, false)
            )
        }

        super.onCreate(savedInstanceState)
        binding = GameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CellAdapter(gameViewModel)
        binding.gameState.text = "Player 1 turn"

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
            // TODO
        }
        binding.exitButton.setOnClickListener { _ ->
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

    fun showControls() {
        binding.cancelExitButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.VISIBLE
        binding.exitButton.visibility = View.VISIBLE
        binding.exitControlsButton.visibility = View.GONE
    }

    fun hideControls() {
        binding.cancelExitButton.visibility = View.GONE
        binding.pauseButton.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
        binding.exitControlsButton.visibility = View.VISIBLE
    }
}