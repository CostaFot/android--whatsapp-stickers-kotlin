package com.feelsokman.androidtemplate.di.module

import com.feelsokman.androidtemplate.ui.activity.MainActivity
import com.feelsokman.androidtemplate.ui.activity.di.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilderModule::class])
    abstract fun mainActivity(): MainActivity
}
