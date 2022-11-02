package com.mdrj.wlt.ui.fragment.branch.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.databinding.FragmentModuleNearbyctrBinding
import me.yokeyword.fragmentation.SupportFragment

class NearbyCtrFragment : SupportFragment(){
    private var _binding : FragmentModuleNearbyctrBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentModuleNearbyctrBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}