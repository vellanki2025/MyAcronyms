package com.albertsons.acronyms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.albertsons.acronyms.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavHost()
    }

    /**
     * Sets up the navigation host fragment for the application.
     */
    private fun setUpNavHost() {
        val host = NavHostFragment.create(R.navigation.main_navigation)
        supportFragmentManager.beginTransaction().replace(
            binding.fragment.id, host
        ).setPrimaryNavigationFragment(host).commit()
    }

}