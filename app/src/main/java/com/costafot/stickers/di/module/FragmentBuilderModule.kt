package com.costafot.stickers.di.module

import com.costafot.stickers.ui.fragments.details.DetailsFragment
import com.costafot.stickers.ui.fragments.details.di.DetailsModule
import com.costafot.stickers.ui.fragments.host.HostFragment
import com.costafot.stickers.ui.fragments.host.di.HostModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [HostModule::class])
    abstract fun hostFragment(): HostFragment

    @ContributesAndroidInjector(modules = [DetailsModule::class])
    abstract fun detailsFragment(): DetailsFragment
}
