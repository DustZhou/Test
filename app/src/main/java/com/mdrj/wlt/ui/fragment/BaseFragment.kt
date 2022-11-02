package com.mdrj.wlt.ui.fragment

import android.content.Context
import com.mdrj.wlt.ui.fragment.branch.BranchFragment
import me.yokeyword.fragmentation.SupportFragment

@Suppress("UNREACHABLE_CODE")
open class BaseFragment : SupportFragment() {
//    protected var _mBackToFirstListener: OnBackToFirstListener? = null
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        _mBackToFirstListener = if (context is OnBackToFirstListener) {
//            context
//        } else {
//            throw RuntimeException(
//                context.toString() + "must implement OnBackToFirstListener"
//            )
//        }
//
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        _mBackToFirstListener = null
//    }

    /**
     *退回事件处理
     */
    override fun onBackPressedSupport(): Boolean {
        return super.onBackPressedSupport()
        if (childFragmentManager.backStackEntryCount > 1) {
            popChild()
        } else {
            if (this is BranchFragment) {
                _mActivity.finish()
            } else {
//                _mBackToFirstListener!!.onBackToFirstListener()
            }
        }
        return true
    }

//    interface OnBackToFirstListener {
//        fun onBackToFirstListener()
//    }
}
