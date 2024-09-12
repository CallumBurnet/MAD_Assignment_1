package com.example.mad_assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import android.app.Activity

import com.example.mad_assignment_1.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val newUserFragmentRequestCode = 1
    private var users = mutableListOf<UserProfile>()
    private lateinit var userViewFragment: UserViewFragment

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
        binding.addNewUser.setOnClickListener{
            val newUserFragment = NewUserFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, newUserFragment).addToBackStack(null).commit()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        System.out.println("DAAAA")

        if (requestCode == newUserFragmentRequestCode && resultCode == Activity.RESULT_OK) {
            val userName = data?.getStringExtra("USER_NAME")
            if (!userName.isNullOrEmpty()) {
                // Add new user to the list and update the adapter
                val newUser = UserProfile(userName, R.drawable.ic_launcher_background) // Replace with actual image resource
                users.add(newUser)
                // Update the RecyclerView in UserViewFragment
                val userViewFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? UserViewFragment
                userViewFragment?.updateUserProfiles(users)
            }
        }
    }



}