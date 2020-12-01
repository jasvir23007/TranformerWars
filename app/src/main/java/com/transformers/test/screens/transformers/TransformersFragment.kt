package com.transformers.test.screens.transformers

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.transformers.test.R
import com.transformers.test.models.transformers.AllTransformers
import com.transformers.test.models.transformers.Transformer
import com.transformers.test.platform.BaseFragment
import com.transformers.test.platform.LiveDataWrapper
import com.transformers.test.viewmodels.TransformersViewModel
import kotlinx.android.synthetic.main.fragment_transformers.*

class TransformersFragment : BaseFragment() {


    lateinit var mRecyclerViewAdapter: TransformersAdapter

    private val mViewModel: TransformersViewModel by lazy {
        ViewModelProvider(this, mBaseViewModelFactory).get(TransformersViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.mViewModel.mTransformersResponse.observe(viewLifecycleOwner, this.mDataObserver)
        this.mViewModel.mDeleteTransformerResponse.observe(viewLifecycleOwner, this.mDeleteObserver)
        this.mViewModel.mLoadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)

        mRecyclerViewAdapter =
            TransformersAdapter(
                requireActivity(),
                arrayListOf(),
                {
                    this.mViewModel.deleteTransformer(it)
                }, {
                    val bundle = Bundle()
                    bundle.putSerializable("transformer", it)
                    findNavController().navigate(R.id.createEditFragment, bundle)
                })

        transformersListRecyclerView.adapter = mRecyclerViewAdapter
        transformersListRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.mViewModel.requestTransformersData()
        add.setOnClickListener {
            findNavController().navigate(R.id.createEditFragment)
        }
        addFirst.setOnClickListener {
            findNavController().navigate(R.id.createEditFragment)
        }
        battle.setOnClickListener {
            if (mViewModel.transformers.none { it.team == "A" } ||
                mViewModel.transformers.none { it.team == "D" }
            ) {
                showToast("There is not enough Transformers for the battle")
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putSerializable("transformers", mViewModel.transformers)
            findNavController().navigate(R.id.battleFragment, bundle)
        }
        exit.setOnClickListener {
            sharedPreferences.edit().putString("token", "").apply()
            findNavController().popBackStack()
            findNavController().navigate(R.id.loginFragment)
        }
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            this.mViewModel.requestTransformersData()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private val mDataObserver = Observer<LiveDataWrapper<AllTransformers>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.ResponseStatus.ERROR -> {
                // Error for http request
                logD("", "LiveDataResult.Status.ERROR = ${result.response}")
                error_holder.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                showToast("Error in getting data")

            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                logD("", "LiveDataResult.Status.SUCCESS = ${result.response}")
                swipeRefresh.isRefreshing = false
                val allTransformers = result.response as AllTransformers
                val listItems = allTransformers.transformers as ArrayList<Transformer>
                if (listItems.isEmpty()) {
                    addFirst.visibility = View.VISIBLE
                    add.visibility = View.GONE
                } else {
                    addFirst.visibility = View.GONE
                    add.visibility = View.VISIBLE
                }
                processData(listItems)
            }
        }
    }

    private val mDeleteObserver = Observer<LiveDataWrapper<Boolean>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.ResponseStatus.LOADING -> {
                // Loading data
            }
            LiveDataWrapper.ResponseStatus.ERROR -> {
                // Error for http request
                logD("", "LiveDataResult.Status.ERROR = ${result.response}")
                showToast("Error in deleting data")

            }
            LiveDataWrapper.ResponseStatus.SUCCESS -> {
                logD("", "LiveDataResult.Status.SUCCESS = ${result.response}")
                this.mViewModel.requestTransformersData()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_transformers

    private fun processData(listItems: ArrayList<Transformer>) {
        logD("", "processData called with data ${listItems.size}")
        logD("", "processData listItems =  $listItems")

        mRecyclerViewAdapter.updateListItems(listItems)

    }

    private val loadingObserver = Observer<Boolean> { visible ->
        if (visible) {
            progress_circular.visibility = View.VISIBLE
        } else {
            progress_circular.visibility = View.INVISIBLE
        }
    }

}