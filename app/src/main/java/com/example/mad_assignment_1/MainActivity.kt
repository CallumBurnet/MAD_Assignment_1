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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CellAdapter
    private val cells = mutableListOf<Cell>();
    var numRows = 6;
    var numCols = 7;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cells.addAll(List(42){index ->
            val row  = index/numCols
            val col = index%numCols
            Cell(row, col,0)
        })
        adapter = CellAdapter(cells){cell ->
            cell.player = 1;
            Toast.makeText(this, "Row: ${cell.row}, Col: ${cell.col}, Player: ${cell.player}", Toast.LENGTH_SHORT).show()

        }
        binding.recyclerView.layoutManager = GridLayoutManager(this,numCols )
        binding.recyclerView.adapter = adapter


    }

}