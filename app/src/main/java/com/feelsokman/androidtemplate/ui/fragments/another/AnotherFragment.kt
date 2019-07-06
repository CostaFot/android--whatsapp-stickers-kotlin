package com.feelsokman.androidtemplate.ui.fragments.another

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.feelsokman.androidtemplate.R
import com.feelsokman.androidtemplate.ui.activity.viewmodel.MainViewModel
import com.feelsokman.androidtemplate.ui.base.BaseFragment
import com.feelsokman.androidtemplate.ui.fragments.another.viewmodel.AnotherViewModel
import com.feelsokman.androidtemplate.ui.fragments.another.viewmodel.AnotherViewModelFactory
import com.feelsokman.storage.Storage
import timber.log.Timber
import javax.inject.Inject

class AnotherFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_another, container, false)
    }

    private val args: AnotherFragmentArgs by navArgs()
    @Inject
    internal lateinit var storage: Storage
    @Inject
    internal lateinit var factory: AnotherViewModelFactory

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModelAnother by viewModels<AnotherViewModel>({ this }, { factory })
    // Get a reference to the ViewModel scoped to its Activity
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Retrieving arguments if any
        val stringArgument = args.extraAnotherFragment
        Timber.tag("NavigationLogger").d("Retrieving argument $stringArgument")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelAnother.observeStringFromStorage()

        activityViewModel.textData.observe(viewLifecycleOwner, Observer {
            Timber.tag("NavigationLogger").e("AnotherFragment Activity string is $it")
        })

        viewModelAnother.textData.observe(viewLifecycleOwner, Observer { stringFromStorage ->
            Timber.tag("NavigationLogger").e("AnotherFragment storage string is $stringFromStorage")
        })
    }
}