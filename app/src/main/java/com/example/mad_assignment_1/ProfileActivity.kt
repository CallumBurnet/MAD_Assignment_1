package com.example.mad_assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer

import com.example.mad_assignment_1.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private var users = mutableListOf<UserProfile>()
    private lateinit var userViewFragment: UserViewFragment
    private val userViewModel : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            userViewFragment = UserViewFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UserViewFragment())
                .commit()
        }




    }





}