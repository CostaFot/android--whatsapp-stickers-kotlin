package com.feelsokman.stickers.di.module

import com.feelsokman.stickers.contentprovider.utils.ContentFileParser
import dagger.Module
import dagger.Provides

@Module
class ContentProviderModule {

    @Provides
    internal fun providesContentFileParser(): ContentFileParser = ContentFileParser()
}