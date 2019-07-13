package com.costafot.stickers.di.module

import com.costafot.stickers.ui.fragments.another.AnotherFragment
import com.costafot.stickers.ui.fragments.another.di.AnotherModule
import com.costafot.stickers.ui.fragments.host.HostFragment
import com.costafot.stickers.ui.fragments.host.di.HostModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [HostModule::class])
    abstract fun hostFragment(): HostFragment

    @ContributesAndroidInjector(modules = [AnotherModule::class])
    abstract fun anotherFragment(): AnotherFragment
}
