package com.costafot.stickers.ui.fragments.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.costafot.stickers.R
import com.costafot.stickers.model.TextItem
import com.costafot.stickers.ui.activity.viewmodel.MainViewModel
import com.costafot.stickers.ui.base.BaseFragment
import com.costafot.stickers.ui.fragments.host.viewmodel.HostViewModel
import com.costafot.stickers.ui.fragments.host.viewmodel.HostViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_host.*
import javax.inject.Inject

class HostFragment : BaseFragment(), AdapterParent.Callback {
    override fun onSeeMoreClicked(position: Int) {
        activityViewModel.updateDetailsStickerPack(position)
        findNavController().navigate(R.id.action_hostFragment_to_detailsFragment)
    }

    override fun onAddButtonClicked(identifier: String, name: String) {
        activityViewModel.tryToAddStickerPack(identifier, name)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_host, container, false)
    }

    @Inject
    internal lateinit var factory: HostViewModelFactory

    private val viewModelHost by viewModels<HostViewModel>({ this }, { factory })
    private val activityViewModel by activityViewModels<MainViewModel>()

    private lateinit var groupAdapter: GroupAdapter<GroupieViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter(view)

        // activityViewModel.stickerData.observe(viewLifecycleOwner, Observer(this::onResult))

        activityViewModel.testit.observe(viewLifecycleOwner, Observer(this::onResult))
    }

    private fun onResult(recyclerItemList: List<TextItem>?) {
        if (recyclerItemList != null) {
            groupAdapter.update(recyclerItemList)
        }
    }

    private fun setupAdapter(view: View) {
        groupAdapter = GroupAdapter()
        // set up the layout manager and set the adapter
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, view ->
            activityViewModel.repeat()
        }
    }

    /*  adapterParent = AdapterParent(this)
      recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
      recyclerView.adapter = adapterParent*/
}
