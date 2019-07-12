package com.feelsokman.stickers.ui.fragments.host

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.feelsokman.stickers.R
import com.feelsokman.stickers.contentprovider.model.StickerPack
import com.feelsokman.stickers.ui.activity.viewmodel.MainViewModel
import com.feelsokman.stickers.ui.base.BaseFragment
import com.feelsokman.stickers.ui.fragments.host.viewmodel.HostViewModel
import com.feelsokman.stickers.ui.fragments.host.viewmodel.HostViewModelFactory
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_host.*
import javax.inject.Inject

class HostFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_host, container, false)
    }

    @Inject
    internal lateinit var factory: HostViewModelFactory

    private val viewModelHost by viewModels<HostViewModel>({ this }, { factory })
    private val activityViewModel by activityViewModels<MainViewModel>()

    private lateinit var adapterMaster: AdapterMaster

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter(view)
        viewModelHost.loadStickers()

        viewModelHost.errorMessage.observe(viewLifecycleOwner, Observer {
            Toasty.success(view.context, it).show()
        })

        viewModelHost.stickerData.observe(viewLifecycleOwner, Observer { stickerPackList ->
            onResult(stickerPackList)
        })
    }

    private fun onResult(stickerPackList: List<StickerPack>?) {
        if (stickerPackList != null) {
            adapterMaster.submitList(stickerPackList)
        }
    }

    private fun setupAdapter(view: View) {
        adapterMaster = AdapterMaster()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(view.context, 1)
        }
        recyclerView.adapter = adapterMaster
    }
}