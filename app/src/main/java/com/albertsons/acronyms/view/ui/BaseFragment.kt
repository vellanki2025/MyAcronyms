package com.albertsons.acronyms.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * A base fragment class that provides common functionality for fragments.
 *
 * @param layoutResourceId The layout resource ID to be inflated for this fragment.
 * @constructor Creates an instance of [BaseFragment].
 */
abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes val layoutResourceId: Int) : Fragment(layoutResourceId) {

    /** The root view of the fragment. */
    private lateinit var mRootView: View

    /** The data binding instance for this fragment's layout. */
    protected lateinit var binding: T

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI will be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        mRootView = binding.root
        mRootView.isClickable = true
        mRootView.isFocusable = true
        return mRootView
    }

    /**
     * Called immediately after the view created by onCreateView(View, ViewGroup, Bundle) has been created and returned.
     *
     * @param view The created view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragmentUI(mRootView)
    }

    /**
     * Set up the UI of the fragment.
     *
     * @param view The root view of the fragment's layout.
     */
    abstract fun setUpFragmentUI(view: View?)

    /**
     * Called when the fragment's view is being destroyed.
     * Removes the root view from its parent view if it has one.
     */
    override fun onDestroyView() {
        if (mRootView.parent != null) {
            (mRootView.parent as ViewGroup).removeView(mRootView)
        }
        super.onDestroyView()
    }
}