package com.example.plogging.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.plogging.R
import com.example.plogging.common.hasNotificationPermission
import com.example.plogging.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

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
        if (this.hasNotificationPermission()) createNotification()
        else requestNotificationPermission.launch(
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        when (result) {
            true -> createNotification()
            false -> Snackbar.make(binding.root, "위치정보를 사용하시려면 알람을 꼭 허용해주세요", 500).show()
        }
    }

    private fun createNotification(){
        // TODO
    }
}