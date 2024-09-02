package com.example.mad_assignment_1
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment_1.databinding.CellViewBinding

class CellAdapter(private val cells: List<Cell>, private val onCellClick: (Cell) -> Unit) : RecyclerView.Adapter<CellAdapter.CellViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = CellViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(cells[position], onCellClick)
    }

    override fun getItemCount(): Int = cells.size

    class CellViewHolder(private val binding: CellViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cell: Cell, onItemClick: (Cell) -> Unit) {

            binding.imageView.setImageResource(cell.imageResId)
            binding.root.setOnClickListener{onItemClick(cell)}
        }
    }
}
