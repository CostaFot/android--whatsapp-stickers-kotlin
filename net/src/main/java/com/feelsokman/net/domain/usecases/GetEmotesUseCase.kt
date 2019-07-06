package com.feelsokman.net.domain.usecases

import com.feelsokman.domain.error.DataSourceErrorKind
import com.feelsokman.net.domain.error.DataSourceError
import com.feelsokman.net.domain.error.RetrofitErrorMapper
import com.feelsokman.net.net.EmoteApi
import com.feelsokman.net.net.model.NetResponseEmote
import com.feelsokman.net.net.resolver.NetworkResolver
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class GetEmotesUseCase(
    private val emoteApi: EmoteApi,
    private val scheduler: Scheduler,
    private val errorMapper: RetrofitErrorMapper,
    private val networkResolver: NetworkResolver
) : BaseDisposableUseCase() {

    override val compositeDisposable = CompositeDisposable()
    override var latestDisposable: Disposable? = null

    fun getEmotes(callback: Callback<NetResponseEmote>) {

        if (networkResolver.isConnected()) {
            compositeDisposable.add(
                emoteApi.getEmotes()
                    .subscribeOn(scheduler)
                    .doOnSubscribe { callback.onLoadingStarted() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            callback.onSuccess(it)
                        },
                        {
                            val error = errorMapper.convert(it)
                            callback.onError(error)
                        }
                    )
            )
        } else {
            callback.onError(DataSourceError(null, DataSourceErrorKind.NO_NETWORK))
        }
    }

    override fun stopAllBackgroundWork() {
        compositeDisposable.clear()
    }
}