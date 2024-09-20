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
        const val GAME_ID = "com.game_activity.gameID"
        const val IS_SINGLE_PLAYER = "com.game_activity.is_single_player"
        const val PLAYER_ONE_COLOUR = "com.game_activity.player_one_colour"
        const val PLAYER_TWO_COLOUR = "com.game_activity.player_two_colour"
    }
    private val gameViewModel: GameInformationModel by viewModels {
        GameInformationModel.Factory
    }
    private var isUserTurn = true // Flag to track if it's the user's turn
    private lateinit var binding: GameBinding
    private lateinit var adapter: CellAdapter

    private val cells = mutableListOf<Cell>()
    private var numRows = 7; //hard coded for testing
    private var numCols = 6; //hard coded for testing
    private lateinit var primaryUser : UserEntity
    private lateinit var secondaryUser : UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {

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

        val primaryUserID = gameViewModel.getPrimaryUserId()
        primaryUser = gameViewModel.getPlayerById(primaryUserID) ?: UserEntity(0, "Player 1", R.drawable.avatar, 0, 0)
        val secondaryUserID = gameViewModel.getSecondaryUserId()
        System.out.println("Second user" +secondaryUserID )

        secondaryUser = gameViewModel.getPlayerById(secondaryUserID) ?: UserEntity(0, "Player 2", R.drawable.avatar, 0, 0)
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
        gameViewModel.win.observe(this) { win ->
            if (win) {
                val winnerID = if (gameViewModel.playerTurn.value == 1) {
                    gameViewModel.getPrimaryUserId()?: return@observe


                } else {
                    gameViewModel.getSecondaryUserId()?: return@observe
                }

                val loserID = if (winnerID == gameViewModel.getPrimaryUserId()) {
                    gameViewModel.getSecondaryUserId()
                } else {
                    gameViewModel.getPrimaryUserId()
                }

                if (loserID != null) {
                    gameViewModel.updateUserStatistics(winnerID, loserID)
                }
                var player = gameViewModel.getPlayerById(winnerID)
                binding.gameState.text = "${player?.name} has won !!"

            }
        }


        gameViewModel.playerTurn.observe(this) { turn ->
            if (gameViewModel.win.value != true && gameViewModel.draw.value != true) {
                if(turn == 1){

                    binding.gameState.text = "${primaryUser.name}'s Turn"  // Assuming you have a TextView for player name

                }else{

                    binding.gameState.text = "${secondaryUser.name}'s Turn"
                }
            }
        }
        gameViewModel.draw.observe(this){ isDraw ->
            if(isDraw){
                binding.gameState.text = "Its a draw"
            }

        }

        if (gameViewModel.isSinglePlayer.value == true) {
            gameViewModel.playerTurn.observe(this) { turn ->
                if (turn == 2) {
                    // Use a Handler to introduce a delay
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        do {
                            val dropPoint = Random.nextInt(0, gameViewModel.numCols - 1)
                        } while (gameViewModel.dropDisc(0, dropPoint))
                        gameViewModel.updateBoard()
                        // Delay for 2000 milliseconds (2 seconds)

                    }, 1000)
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