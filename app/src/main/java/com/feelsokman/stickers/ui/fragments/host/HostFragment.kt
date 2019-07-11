package com.feelsokman.stickers.ui.fragments.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feelsokman.stickers.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelHost.errorMessage.observe(viewLifecycleOwner, Observer {
            Toasty.success(view.context, it).show()
        })

        viewModelHost.stickerData.observe(viewLifecycleOwner, Observer { stickerPackList ->
            Toasty.success(view.context, "SUCCESS ${stickerPackList[0].stickers!!.size}").show()
        })

        button.setOnClickListener {
            viewModelHost.loadStickers()
        }
    }
}