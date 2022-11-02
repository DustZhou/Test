package com.mdrj.wlt.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.databinding.ActivityModuleMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityModuleMainBinding = ActivityModuleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBranch.setOnClickListener {
            val intent = Intent(this@MainActivity,BranchActivity::class.java)
            startActivity(intent)
        }
    }

}