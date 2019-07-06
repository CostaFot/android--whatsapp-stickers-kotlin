package com.feelsokman.androidtemplate.di.component

import android.app.Application
import com.feelsokman.androidtemplate.TemplateApplication
import com.feelsokman.androidtemplate.di.module.ActivityBuilderModule
import com.feelsokman.androidtemplate.di.module.AppModule
import com.feelsokman.androidtemplate.di.module.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class,
        UseCaseModule::class
    ]
)
interface AppComponent : AndroidInjector<TemplateApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
