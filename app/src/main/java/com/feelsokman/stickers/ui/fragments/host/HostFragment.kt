package com.feelsokman.stickers.ui.fragments.host

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feelsokman.net.domain.error.DataSourceError
import com.feelsokman.net.domain.usecases.BaseDisposableUseCase
import com.feelsokman.stickers.R
import com.feelsokman.stickers.contentprovider.model.StickerPack
import com.feelsokman.stickers.ui.activity.viewmodel.MainViewModel
import com.feelsokman.stickers.ui.base.BaseFragment
import com.feelsokman.stickers.ui.fragments.host.viewmodel.HostViewModel
import com.feelsokman.stickers.ui.fragments.host.viewmodel.HostViewModelFactory
import com.feelsokman.stickers.usecase.StickerPackLoaderUseCase
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_host.*
import timber.log.Timber
import javax.inject.Inject

class HostFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_host, container, false)
    }

    @Inject
    internal lateinit var factory: HostViewModelFactory
    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModelHost by viewModels<HostViewModel>({ this }, { factory })
    // Get a reference to the ViewModel scoped to its Activity
    private val activityViewModel by activityViewModels<MainViewModel>()

    @Inject
    internal lateinit var stickerPackLoaderUseCase: StickerPackLoaderUseCase

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.textData.observe(viewLifecycleOwner, Observer {
            Timber.tag("NavigationLogger").e("HostFragment Activity string is $it")
        })

        viewModelHost.textData.observe(viewLifecycleOwner, Observer {
            Timber.tag("NavigationLogger").e("HostFragment viewmodel string is $it")
        })

        button.setOnClickListener {
            stickerPackLoaderUseCase.loadStickerPacks(object : BaseDisposableUseCase.Callback<ArrayList<StickerPack>> {
                override fun onLoadingStarted() {
                    //
                }

                override fun onSuccess(result: ArrayList<StickerPack>) {
                    Toasty.success(view.context, "SUCCESS ${result[0].stickers!!.size}").show()
                }

                override fun onError(error: DataSourceError) {
                    Toasty.error(view.context, error.errorMessage.toString()).show()
                }
            })
            // findNavController().navigate(R.id.action_hostFragment_to_anotherFragment)
        }
    }

    override fun onPause() {
        super.onPause()
    }
}