package com.transformers.test.screens.transformers

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.transformers.test.R
import com.transformers.test.models.transformers.Transformer
import com.transformers.test.platform.BaseFragment
import com.transformers.test.platform.LiveDataWrapper
import com.transformers.test.viewmodels.TransformersViewModel
import kotlinx.android.synthetic.main.fragment_create_edit.*
import kotlinx.android.synthetic.main.fragment_create_edit.progress_circular
import java.lang.Exception


class CreateEditFragment : BaseFragment() {

    private val mViewModel: TransformersViewModel by lazy {
        ViewModelProvider(this, mBaseViewModelFactory).get(TransformersViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.mViewModel.mCreateTransformerResponse.observe(viewLifecycleOwner, this.mCreateObserver)
        this.mViewModel.mUpdateTransformerResponse.observe(viewLifecycleOwner, this.mUpdateObserver)
        this.mViewModel.mLoadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argTransformer = arguments?.getSerializable("transformer") as? Transformer
        argTransformer?.let {
            mViewModel.transformer = it

            name.setText(it.name)
            team.setText(it.team)
            rank.setText(it.rank.toString())
            strength.setText(it.strength.toString())
            courage.setText(it.courage.toString())
            endurance.setText(it.endurance.toString())
            firepower.setText(it.firepower.toString())
            intelligence.setText(it.intelligence.toString())
            speed.setText(it.speed.toString())
            skill.setText(it.skill.toString())

            title.text = "Edit"
            actionBtn.text = "Update"
        }

        actionBtn.setOnClickListener {
            try {
                mViewModel.transformer.name = name.text.toString()
                mViewModel.transformer.team = team.text.toString()
                mViewModel.transformer.rank = rank.text.toString().toInt()
                mViewModel.transformer.strength = strength.text.toString().toInt()
                mViewModel.transformer.courage = courage.text.toString().toInt()
                mViewModel.transformer.endurance = endurance.text.toString().toInt()
                mViewModel.transformer.firepower = firepower.text.toString().toInt()
                mViewModel.transformer.intelligence = intelligence.text.toString().toInt()
                mViewModel.transformer.speed = speed.text.toString().toInt()
                mViewModel.transformer.skill = skill.text.toString().toInt()
            } catch (e: Exception) {
                showToast("Fill all the required fields")
                return@setOnClickListener
            }
            if (mViewModel.transformer.id.isNullOrEmpty()) {
                mViewModel.createTransformer()
            } else {
                mViewModel.updateTransformer()
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_create_edit

    private val mUpdateObserver = Observer<LiveDataWrapper<Transformer>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.ResponseStatus.ERROR -> {
                // Error for http request
                logD("", "LiveDataResult.Status.ERROR = ${result.error}")
                hideKeyboard()
                showToast(result.error?.message.toString())

            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                logD("", "LiveDataResult.Status.SUCCESS = ${result.response}")
                hideKeyboard()
                findNavController().popBackStack()
            }
        }
    }

    private val mCreateObserver = Observer<LiveDataWrapper<Transformer>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.ResponseStatus.ERROR -> {
                // Error for http request
                logD("", "LiveDataResult.Status.ERROR = ${result.error}")
                hideKeyboard()
                showToast(result.error?.message.toString())

            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                logD("", "LiveDataResult.Status.SUCCESS = ${result.response}")
                hideKeyboard()
                findNavController().popBackStack()
            }
        }
    }

    private val loadingObserver = Observer<Boolean> { visible ->
        if (visible) {
            progress_circular.visibility = View.VISIBLE
        } else {
            progress_circular.visibility = View.INVISIBLE
        }
    }
}