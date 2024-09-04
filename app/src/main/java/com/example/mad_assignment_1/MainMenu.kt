package com.example.mad_assignment_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.mad_assignment_1.databinding.MainMenuBinding

class MainMenu : AppCompatActivity() {
    private lateinit var binding: MainMenuBinding
    private lateinit var menuFragment: Menu
    private lateinit var newGameFragment: NewGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMenuBinding.inflate(layoutInflater)
        menuFragment = Menu()
        newGameFragment = NewGame()
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadMenuFragment()
        }
    }

    private fun loadMenuFragment() {
        val fm = supportFragmentManager;
        fm.beginTransaction().replace(R.id.mainMenuContainer, menuFragment).commit()
    }

    private fun loadNewGameFragment() {
        val fm = supportFragmentManager;
        val frag = fm.findFragmentById(R.id.mainMenuContainer);
        if (frag != null) {
            fm.beginTransaction().replace(R.id.mainMenuContainer, newGameFragment).commit()
        }
    }
}