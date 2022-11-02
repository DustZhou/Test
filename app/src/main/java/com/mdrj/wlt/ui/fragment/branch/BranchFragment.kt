package com.mdrj.wlt.ui.fragment.branch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.databinding.FragmentModuleBranchBinding
import com.mdrj.wlt.ui.fragment.BaseFragment


class BranchFragment : BaseFragment() {
    private var _binding: FragmentModuleBranchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModuleBranchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
    }

    companion object {
        fun newInstance(): BaseFragment {
            val bundle = Bundle()
            val goodsFragment = BaseFragment()
            goodsFragment.arguments = bundle
            return goodsFragment
        }
    }
}