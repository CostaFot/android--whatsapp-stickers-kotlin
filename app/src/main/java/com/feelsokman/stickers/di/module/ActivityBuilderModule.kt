package com.feelsokman.stickers.di.module

import com.feelsokman.stickers.contentprovider.StickerContentProvider
import com.feelsokman.stickers.ui.activity.MainActivity
import com.feelsokman.stickers.ui.activity.di.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilderModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun stickeStickerContentProvider(): StickerContentProvider
}
