package com.example.mad_assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment_1.databinding.FragmentNewGameBinding


class NewGame : Fragment() {
    private lateinit var binding: FragmentNewGameBinding
    private lateinit var menuViewModel: MenuInformationModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewGameBinding.inflate(inflater, container, false)
        menuViewModel = ViewModelProvider(requireActivity())[MenuInformationModel::class.java]
        when (menuViewModel.gridSize.value) {
            MenuInformationModel.GridSize.SMALL -> binding.smallGridRadio.isChecked = true
            MenuInformationModel.GridSize.STANDARD -> binding.standardGridRadio.isChecked = true
            MenuInformationModel.GridSize.LARGE -> binding.largeGridRadio.isChecked = true

            else -> binding.standardGridRadio.isChecked = true
        }
        binding.launchGameButton.setOnClickListener { view ->
            val intent = Intent(view.context, GameActivity::class.java)
            when (menuViewModel.gridSize.value) {
                MenuInformationModel.GridSize.SMALL -> {
                    intent.putExtra(GameActivity.GRID_ROWS, 6)
                    intent.putExtra(GameActivity.GRID_COLS, 5)
                }
                MenuInformationModel.GridSize.STANDARD -> {
                    intent.putExtra(GameActivity.GRID_ROWS, 7)
                    intent.putExtra(GameActivity.GRID_COLS, 6)
                }
                MenuInformationModel.GridSize.LARGE -> {
                    intent.putExtra(GameActivity.GRID_ROWS, 8)
                    intent.putExtra(GameActivity.GRID_COLS, 7)
                }
                // Default to standard size
                else -> {
                    intent.putExtra(GameActivity.GRID_ROWS, 7)
                    intent.putExtra(GameActivity.GRID_COLS, 6)
                }
            }
            startActivity(intent)
        }
        binding.smallGridRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                menuViewModel.setGridSize(MenuInformationModel.GridSize.SMALL);
            }
        }
        binding.standardGridRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                menuViewModel.setGridSize(MenuInformationModel.GridSize.STANDARD);
            }
        }
        binding.largeGridRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                menuViewModel.setGridSize(MenuInformationModel.GridSize.LARGE);
            }
        }
        binding.newGameBackButton.setOnClickListener { view ->
            val fm = requireActivity().supportFragmentManager;
            fm.beginTransaction().replace(R.id.mainMenuContainer, Menu()).commit()
        }
        return binding.root
    }
}