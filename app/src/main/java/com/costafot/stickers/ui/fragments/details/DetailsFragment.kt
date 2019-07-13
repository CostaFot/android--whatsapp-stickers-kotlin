package com.costafot.stickers.ui.fragments.details

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.costafot.stickers.R
import com.costafot.stickers.contentprovider.model.StickerPack
import com.costafot.stickers.ui.activity.viewmodel.MainViewModel
import com.costafot.stickers.ui.base.BaseFragment
import com.costafot.stickers.ui.fragments.details.viewmodel.DetailsViewModel
import com.costafot.stickers.ui.fragments.details.viewmodel.DetailsViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @Inject
    internal lateinit var factory: DetailsViewModelFactory

    private val viewModelDetails by viewModels<DetailsViewModel>({ this }, { factory })
    private val activityViewModel by activityViewModels<MainViewModel>()

    private lateinit var detailsAdapter: DetailsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.detailsStickerPackData.observe(viewLifecycleOwner, Observer {
            onResult(view, it)
        })
    }

    private fun onResult(view: View, stickerPack: StickerPack) {
        detailsAdapter = DetailsAdapter(stickerPack.identifier!!)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recycleView_details.layoutManager = GridLayoutManager(view.context, 5)
        } else {
            recycleView_details.layoutManager = GridLayoutManager(view.context, 4)
        }
        recycleView_details.adapter = detailsAdapter

        if (!stickerPack.stickers.isNullOrEmpty()) {
            detailsAdapter.submitList(stickerPack.stickers)
        }
    }
}