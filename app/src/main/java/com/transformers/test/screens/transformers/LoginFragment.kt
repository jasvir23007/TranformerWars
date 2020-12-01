package com.transformers.test.screens.transformers

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.transformers.test.R
import com.transformers.test.platform.BaseFragment
import com.transformers.test.platform.LiveDataWrapper
import com.transformers.test.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private val mViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, mBaseViewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.mViewModel.mAllSparksResponse.observe(viewLifecycleOwner, this.mDataObserver)
        this.mViewModel.mLoadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val token = sharedPreferences.getString("token", "")
        if (!token.isNullOrEmpty()) {
            findNavController().popBackStack()
            findNavController().navigate(R.id.transformersFragment)
        }
        start.setOnClickListener {
            this.mViewModel.requestAllSparksData()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private val mDataObserver = Observer<LiveDataWrapper<String>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.ResponseStatus.ERROR -> {
                // Error for http request
                logD("", "LiveDataResult.Status.ERROR = ${result.response}")
                showToast("Error in getting data")

            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                logD("", "LiveDataResult.Status.SUCCESS = ${result.response}")
                val token = result.response as String
                processData(token)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_login

    private fun processData(token: String) {
        logD("", "processData called with data $token")
        sharedPreferences.edit().putString("token", token).apply()
        findNavController().popBackStack()
        findNavController().navigate(R.id.transformersFragment)
    }

    private val loadingObserver = Observer<Boolean> { visible ->
        if (visible) {
            progress_circular.visibility = View.VISIBLE
        } else {
            progress_circular.visibility = View.INVISIBLE
        }
    }
}