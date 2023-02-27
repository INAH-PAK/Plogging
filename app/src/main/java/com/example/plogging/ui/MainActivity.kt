package com.example.plogging.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.plogging.R
import com.example.plogging.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {
        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        navController?.let {
            binding.homeBottomNavigation.setupWithNavController(it)
        }
    }
}