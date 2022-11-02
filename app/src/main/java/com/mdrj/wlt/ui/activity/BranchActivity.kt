package com.mdrj.wlt.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager.findFragment
import com.example.test.databinding.ActivityModuleBranchBinding
import com.mdrj.wlt.ui.fragment.branch.BranchFragment
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment

class BranchActivity: SupportActivity() {
    private var mfragment = SupportFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityModuleBranchBinding = ActivityModuleBranchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadRootFragment(binding.flBranch.id,BranchFragment())
    }
}