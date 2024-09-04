package com.example.mad_assignment_1

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuFragment: Menu
    private lateinit var newGameFragment: NewGame
    private val menuViewModel: MenuInformationModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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