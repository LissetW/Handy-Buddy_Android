package com.lnd.handybuddy.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lnd.handybuddy.R
import com.lnd.handybuddy.databinding.ActivityMainBinding
import com.lnd.handybuddy.utils.NetworkMonitor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.signUpFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }

        binding.bottomNav.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.searchFragment,
                R.id.chatFragment,
                R.id.profileFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        networkMonitor = NetworkMonitor(this) {
            supportFragmentManager.fragments.forEach { fragment ->
                if(fragment is NetworkAware) {
                    fragment.onNetworkAvailable()
                }
            }
        }
        networkMonitor.register()
    }

    override fun onPause() {
        super.onPause()
        networkMonitor.unregister()
    }
}