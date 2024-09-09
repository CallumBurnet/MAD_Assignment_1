package com.example.mad_assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.CellViewBinding

class CellAdapter(private var cells: List<Cell>, private val onCellClick: (Cell) -> Unit) : RecyclerView.Adapter<CellAdapter.CellViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = CellViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(cells[position], onCellClick)
    }

    override fun getItemCount(): Int = cells.size


    fun updateCells(newCells: List<Cell>) {
        cells = newCells
        notifyDataSetChanged()
    }
    class CellViewHolder(private val binding: CellViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cell: Cell, onItemClick: (Cell) -> Unit) {
            if (cell.player == 1) {
                binding.playerOneCoin.visibility = View.VISIBLE
            }else {
                binding.playerOneCoin.visibility = View.GONE
            }


            binding.root.setOnClickListener{

                onItemClick(cell)
                if (cell.player == 1) {
                    binding.playerTwoCoin.visibility = View.GONE
                    binding.playerOneCoin.visibility = View.VISIBLE
                } else if(cell.player == 2){
                    binding.playerOneCoin.visibility = View.GONE
                    binding.playerTwoCoin.visibility = View.VISIBLE
                }
            }

        }
    }
}
