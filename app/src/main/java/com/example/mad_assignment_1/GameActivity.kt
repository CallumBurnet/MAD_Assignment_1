package com.example.mad_assignment_1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        const val GAME_ID = "com.game_activity.gameID" // Key for game ID in intents
        const val IS_SINGLE_PLAYER = "com.game_activity.is_single_player" // Key for single player mode
        const val PLAYER_ONE_COLOUR = "com.game_activity.player_one_colour" // Key for Player One's color
        const val PLAYER_TWO_COLOUR = "com.game_activity.player_two_colour" // Key for Player Two's color
    }

    private val gameViewModel: GameInformationModel by viewModels { GameInformationModel.Factory }
    private lateinit var binding: GameBinding
    private lateinit var adapter: CellAdapter
    private lateinit var primaryUser: UserEntity
    private lateinit var secondaryUser: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize game state if not restoring from a saved instance
        if (savedInstanceState == null) {
            Log.d("GameActivity", "Found Grid rows and cols")
            gameViewModel.init(
                intent.getLongExtra(GAME_ID, 0),
                intent.getBooleanExtra(GameActivity.IS_SINGLE_PLAYER, false)
            )
            gameViewModel.playerOneColor = intent.getIntExtra(PLAYER_ONE_COLOUR, Color.BLUE)
            gameViewModel.playerTwoColor = intent.getIntExtra(PLAYER_TWO_COLOUR, Color.RED)
        }

        super.onCreate(savedInstanceState)
        binding = GameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve user data for primary and secondary players
        val primaryUserID = gameViewModel.getPrimaryUserId()
        primaryUser = gameViewModel.getPlayerById(primaryUserID) ?: UserEntity(0, "Player 1", R.drawable.avatar, 0, 0)
        val secondaryUserID = gameViewModel.getSecondaryUserId()
        secondaryUser = gameViewModel.getPlayerById(secondaryUserID) ?: UserEntity(0, "Player 2", R.drawable.avatar, 0, 0)

        // Set up the cell adapter for the game board
        adapter = CellAdapter(gameViewModel)

        // Set up button click listeners
        binding.undoButton.setOnClickListener { view ->
            if (!gameViewModel.undo()) {
                Snackbar.make(view, "No moves to undo", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.resetButton.setOnClickListener { _ -> gameViewModel.reset() }
        binding.exitControlsButton.setOnClickListener { _ -> hideGameControls(); showControls() }
        binding.cancelExitButton.setOnClickListener { _ -> showGameControls(); hideControls() }
        binding.pauseButton.setOnClickListener { _ -> hideControls(); gameViewModel.saveToDatabase(); setResult(RESULT_OK); finish() }
        binding.exitButton.setOnClickListener { _ -> hideControls(); gameViewModel.dropGame(); setResult(RESULT_OK); finish() }

        // Observe game state for winning condition
        gameViewModel.win.observe(this) { win ->
            if (win) {
                val winnerID = if (gameViewModel.playerTurn.value == 1) {
                    gameViewModel.getPrimaryUserId() ?: return@observe
                } else {
                    gameViewModel.getSecondaryUserId() ?: return@observe
                }

                val loserID = if (winnerID == gameViewModel.getPrimaryUserId()) {
                    gameViewModel.getSecondaryUserId()
                } else {
                    gameViewModel.getPrimaryUserId()
                }

                if (loserID != null) {
                    gameViewModel.updateUserStatistics(winnerID, loserID) // Update statistics for users
                }
                val player = gameViewModel.getPlayerById(winnerID)
                binding.gameState.text = "${player?.name} has won !!" // Display winner
            }
        }

        // Observe remaining moves and update UI
        gameViewModel.numRemainingMoves.observe(this) { moves ->
            binding.numRemainingMoves.text = "${moves} moves remaining"
        }
        gameViewModel.numMovesPlayed.observe(this) { moves ->
            binding.numMovesPlayed.text = "${moves} moves played"
        }

        // Observe player's turn and update game state display
        gameViewModel.playerTurn.observe(this) { turn ->
            if (gameViewModel.win.value != true && gameViewModel.draw.value != true) {
                binding.gameState.text = if (turn == 1) {
                    "${primaryUser.name}'s Turn" // Player One's turn
                } else {
                    "${secondaryUser.name}'s Turn" // Player Two's turn
                }
            }
        }

        // Observe draw condition
        gameViewModel.draw.observe(this) { isDraw ->
            if (isDraw) {
                binding.gameState.text = "It's a draw !!" // Display draw message
            }
        }

        // Single player mode AI logic
        if (gameViewModel.isSinglePlayer.value == true) {
            var isProcessingRequest = false // Flag to track request processing

            gameViewModel.playerTurn.observe(this) { turn ->
                if (turn == 2 && !isProcessingRequest && gameViewModel.win.value == false) { // Check if it's AI's turn
                    isProcessingRequest = true // Set flag to indicate request in progress

                    // Use a Handler to introduce a delay before AI makes a move
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        do {
                            val dropPoint = Random.nextInt(0, gameViewModel.numCols - 1) // Choose random column
                        } while (gameViewModel.dropDisc(0, dropPoint))

                        gameViewModel.updateBoard() // Update the board after the move
                        isProcessingRequest = false // Reset flag after processing
                    }, 1000)
                }
            }
        }

        // Set up RecyclerView for the game board
        binding.recyclerView.layoutManager = GridLayoutManager(this, gameViewModel.numCols)
        binding.recyclerView.adapter = adapter
    }

    // Show control buttons for exiting or pausing the game
    private fun showControls() {
        binding.cancelExitButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.VISIBLE
        binding.exitButton.visibility = View.VISIBLE
        binding.exitControlsButton.visibility = View.GONE
        binding.undoButton.visibility = View.GONE
        binding.resetButton.visibility = View.GONE
    }

    // Hide control buttons
    private fun hideControls() {
        binding.cancelExitButton.visibility = View.GONE
        binding.pauseButton.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
        binding.exitControlsButton.visibility = View.VISIBLE
        binding.undoButton.visibility = View.VISIBLE
        binding.resetButton.visibility = View.VISIBLE
    }

    // Hide game controls (undo/reset/exit buttons)
    private fun hideGameControls() {
        binding.resetButton.visibility = View.GONE
        binding.undoButton.visibility = View.GONE
        binding.exitButton.visibility = View.GONE
    }

    // Show game controls (undo/reset/exit buttons)
    private fun showGameControls() {
        binding.resetButton.visibility = View.VISIBLE
        binding.undoButton.visibility = View.VISIBLE
        binding.exitButton.visibility = View.VISIBLE
    }
}
