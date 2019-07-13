package com.costafot.stickers.di.module

import com.costafot.stickers.contentprovider.StickerContentProvider
import com.costafot.stickers.ui.activity.MainActivity
import com.costafot.stickers.ui.activity.di.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilderModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun stickeStickerContentProvider(): StickerContentProvider
}
