package com.example.mad_assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mad_assignment_1.databinding.FragmentNewGameBinding


class NewGame : Fragment() {
    private lateinit var binding: FragmentNewGameBinding
    private val menuViewModel: MenuInformationModel by viewModels<MenuInformationModel> {
        MenuInformationModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewGameBinding.inflate(inflater, container, false)
        when (menuViewModel.gridSize.value) {
            MenuInformationModel.GridSize.SMALL -> binding.smallGridRadio.isChecked = true
            MenuInformationModel.GridSize.STANDARD -> binding.standardGridRadio.isChecked = true
            MenuInformationModel.GridSize.LARGE -> binding.largeGridRadio.isChecked = true
            else -> binding.standardGridRadio.isChecked = true // Default case
        }

        menuViewModel.isSinglePlayer.observe(viewLifecycleOwner) { isSinglePlayer ->
            // Add null check for isSinglePlayer
            if (isSinglePlayer != null) {
                val hasGames = menuViewModel.checkForGames(1, if (isSinglePlayer) 3 else 2).isNotEmpty()
                binding.resumeButton.visibility = if (hasGames) View.VISIBLE else View.GONE
            }
        }
        binding.launchGameButton.setOnClickListener { view ->
            val intent = Intent(view.context, GameActivity::class.java)
            val rows: Int
            val cols: Int
            val primaryUserId = arguments?.getLong("primaryUserID") ?: 0L
            var secondaryUserId = arguments?.getLong("secondaryUserID") ?: 1L
            // 3 is the default UserID for the computer
            if (menuViewModel.isSinglePlayer.value == true) {
                secondaryUserId = 3;
            }

            when (menuViewModel.gridSize.value) {
                MenuInformationModel.GridSize.SMALL -> {
                    rows = 6
                    cols = 5
                }
                MenuInformationModel.GridSize.STANDARD -> {
                    rows = 7
                    cols = 6
                }
                MenuInformationModel.GridSize.LARGE -> {
                    rows = 8
                    cols = 7
                }
                else -> {
                    rows = 7
                    cols = 6
                }
            }
            val gameID = menuViewModel.newGame(rows, cols, primaryUserId, secondaryUserId)
            intent.putExtra(GameActivity.IS_SINGLE_PLAYER, menuViewModel.isSinglePlayer.value ?: true)
            intent.putExtra(GameActivity.GAME_ID, gameID)
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
        binding.newGameBackButton.setOnClickListener { _ ->
            val fm = requireActivity().supportFragmentManager;
            fm.beginTransaction().replace(R.id.mainMenuContainer, Menu()).commit()
        }
        binding.pvpMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                menuViewModel.setGameMode(false) // Set as multiplayer
            }
        }
        binding.aiMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                menuViewModel.setGameMode(true) // Set as single player
            }
        }
        binding.resumeButton.setOnClickListener { view ->
            val gameID = if (menuViewModel.isSinglePlayer.value == true) {
                menuViewModel.checkForGames(1, 3).first().gameID
            } else {
                menuViewModel.checkForGames(1, 2).first().gameID
            }
            val intent = Intent(view.context, GameActivity::class.java)
            intent.putExtra(GameActivity.IS_SINGLE_PLAYER, menuViewModel.isSinglePlayer.value)
            intent.putExtra(GameActivity.GAME_ID, gameID)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        menuViewModel.forceUpdate()
    }
}