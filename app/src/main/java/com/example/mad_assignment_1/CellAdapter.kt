package com.example.mad_assignment_1

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.CellViewBinding
import com.google.android.material.snackbar.Snackbar

// Adapter for the cells in the Connect Four game
class CellAdapter(private val gameViewModel: GameInformationModel)
    : RecyclerView.Adapter<CellAdapter.CellViewHolder>() {

    // Creates a new ViewHolder for each cell
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        // Inflate the layout for each cell and create a ViewHolder
        val binding = CellViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(
            binding,
            parent.context as LifecycleOwner, // Casting context to LifecycleOwner
            gameViewModel.playerOneColor, // Color for Player One
            gameViewModel.playerTwoColor  // Color for Player Two
        )
    }

    // Binds the cell data to the ViewHolder
    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(gameViewModel.cells.value!![position], gameViewModel)
    }

    // Returns the total number of cells
    override fun getItemCount(): Int = gameViewModel.cells.value!!.size

    // Updates the data for a specific cell based on its row and column
    fun updateData(row: Int, col: Int) {
        notifyItemChanged(row * gameViewModel.numCols + col)
    }

    // ViewHolder class for individual cells
    class CellViewHolder(
        private val binding: CellViewBinding,
        private val lifecycleOwner: LifecycleOwner,
        private val playerOneColor: Int,
        private val playerTwoColor: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        // Binds data from the cell to the views in the ViewHolder
        fun bind(cell: Cell, gameViewModel: GameInformationModel) {
            // Set the color for player coins
            binding.playerOneCoin.imageTintList = ColorStateList.valueOf(playerOneColor)
            binding.playerTwoCoin.imageTintList = ColorStateList.valueOf(playerTwoColor)

            // Show or hide player coins based on the cell state
            when (cell.player) {
                1 -> {
                    binding.playerOneCoin.visibility = View.VISIBLE
                    binding.playerTwoCoin.visibility = View.GONE
                }
                2 -> {
                    binding.playerTwoCoin.visibility = View.VISIBLE
                    binding.playerOneCoin.visibility = View.GONE
                }
                else -> {
                    binding.playerOneCoin.visibility = View.GONE
                    binding.playerTwoCoin.visibility = View.GONE
                }
            }

            // Set click listener for the cell
            binding.root.setOnClickListener { view ->
                // Prevent moves during the AI player's turn in single-player mode
                if (gameViewModel.isSinglePlayer.value == true && gameViewModel.playerTurn.value == 2) {
                    return@setOnClickListener
                }

                // Proceed if the game is not in a winning state
                if (gameViewModel.win.value == false) {
                    // Attempt to drop a disc in the selected column
                    if (!gameViewModel.dropDisc(cell.row, cell.col)) {
                        gameViewModel.updateBoard() // Update the board if the move was valid
                    } else {
                        // Show a message if the move is invalid
                        Snackbar.make(view, "Invalid move", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            // Observe the cells LiveData for changes
            gameViewModel.cells.observe(lifecycleOwner) { cells ->
                // Update the player state based on LiveData
                cell.player = cells[cell.row * gameViewModel.numCols + cell.col].player
                // Update visibility of player coins based on current player state
                when (cell.player) {
                    1 -> {
                        binding.playerTwoCoin.visibility = View.GONE
                        binding.playerOneCoin.visibility = View.VISIBLE
                    }
                    2 -> {
                        binding.playerOneCoin.visibility = View.GONE
                        binding.playerTwoCoin.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.playerOneCoin.visibility = View.GONE
                        binding.playerTwoCoin.visibility = View.GONE
                    }
                }
            }
        }
    }
}
