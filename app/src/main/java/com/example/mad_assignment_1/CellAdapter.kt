package com.example.mad_assignment_1

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.CellViewBinding
import com.google.android.material.snackbar.Snackbar

class CellAdapter(private val gameViewModel: GameInformationModel)
    : RecyclerView.Adapter<CellAdapter.CellViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = CellViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(
            binding,
            parent.context as LifecycleOwner,
            gameViewModel.playerOneColor,
            gameViewModel.playerTwoColor
        )
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(gameViewModel.cells.value!![position], gameViewModel)
    }

    override fun getItemCount(): Int = gameViewModel.cells.value!!.size

    fun updateData(row: Int, col: Int) {
        notifyItemChanged(row * gameViewModel.numCols + col)
    }

    class CellViewHolder(private val binding: CellViewBinding,
                         private val lifecycleOwner: LifecycleOwner,
                         private val playerOneColor: Int,
                         private val playerTwoColor: Int)
    : RecyclerView.ViewHolder(binding.root) {
        fun bind(cell: Cell, gameViewModel: GameInformationModel) {
            binding.playerOneCoin.imageTintList = ColorStateList.valueOf(playerOneColor)
            binding.playerTwoCoin.imageTintList = ColorStateList.valueOf(playerTwoColor)
            if (cell.player == 1) {
                binding.playerOneCoin.visibility = View.VISIBLE
                binding.playerTwoCoin.visibility = View.GONE
            } else if (cell.player == 2) {
                binding.playerTwoCoin.visibility = View.VISIBLE
                binding.playerOneCoin.visibility = View.GONE
            } else {
                binding.playerOneCoin.visibility = View.GONE
                binding.playerTwoCoin.visibility = View.GONE
            }
            binding.root.setOnClickListener { view ->
                // Only allow moves in non-win state
                if (gameViewModel.win.value == false) {
                    if (!gameViewModel.dropDisc(cell.row, cell.col)) {
                        gameViewModel.updateBoard()
                    } else {
                        Snackbar.make(view, "Invalid move", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            gameViewModel.cells.observe(lifecycleOwner) {cells ->
                cell.player = cells[cell.row * gameViewModel.numCols + cell.col].player
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
