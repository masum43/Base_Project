package com.mispran.outlet_order.features.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.mispran.outlet_order.common.dialog.ProgressDialog
import com.mispran.outlet_order.R
import com.mispran.outlet_order.common.extensions.toast
import com.mispran.outlet_order.common.network.model.ResponseState
import com.mispran.outlet_order.databinding.FragmentHomeBinding
import com.wada811.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModel<HomeViewModel>()
    private val progressDialog by lazy { ProgressDialog.Builder(requireContext()).build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

    }

    private fun initObservers() {
        viewModel.api1DataList.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    val nonEmptyCategoryWithCourse = it.data
                }
                is ResponseState.Error -> { /*ignored*/ }
                is ResponseState.Loading -> { /*ignored*/ }
            }
        }


        viewModel.fetchAnyApiData2.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    progressDialog.show()
                }
                is ResponseState.Error -> {
                    progressDialog.dismiss()
                    toast(it.errorMessage)
                }
                is ResponseState.Success -> {
                    progressDialog.dismiss()
                    val response = it.data
                }
            }
        }
    }
}