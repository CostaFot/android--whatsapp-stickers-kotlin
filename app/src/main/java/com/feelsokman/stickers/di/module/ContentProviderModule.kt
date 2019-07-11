package com.feelsokman.stickers.di.module

import com.feelsokman.stickers.usecase.ContentFileParser
import dagger.Module
import dagger.Provides

@Module
class ContentProviderModule {

    @Provides
    internal fun providesContentFileParser(): ContentFileParser =
        ContentFileParser()
}