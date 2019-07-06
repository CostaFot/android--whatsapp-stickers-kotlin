package com.feelsokman.net.net.error;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Taken from https://gist.github.com/julianfalcionelli/a585d2cb2fbaca2e123ae7df3e36fa39
 */
public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    private static class RxCallAdapterWrapper implements CallAdapter {

        private final Retrofit retrofit;
        private final CallAdapter wrapped;

        RxCallAdapterWrapper(Retrofit retrofit, CallAdapter wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings({"unchecked", "NullableProblems"})
        @Override
        public Object adapt(Call call) {
            Object adaptedCall = wrapped.adapt(call);

            if (adaptedCall instanceof Completable) {
                return ((Completable) adaptedCall).onErrorResumeNext(
                        throwable -> Completable.error(asRetrofitException(throwable)));
            }

            if (adaptedCall instanceof Single) {
                return ((Single) adaptedCall).onErrorResumeNext(
                        throwable -> Single.error(asRetrofitException((Throwable) throwable)));
            }

            if (adaptedCall instanceof Observable) {
                return ((Observable) adaptedCall)
                        .onErrorResumeNext(
                                (Function<? super Throwable, ? extends ObservableSource>) throwable -> Observable.error(asRetrofitException(throwable)));
            }

            if (adaptedCall instanceof Maybe) {
                return ((Maybe) adaptedCall).onErrorComplete();
            }

            throw new RuntimeException("Observable Type not supported");
        }

        private RetrofitException asRetrofitException(Throwable throwable) {
            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                return RetrofitException.httpError(response.raw().request().url().toString(),
                        response, retrofit);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return RetrofitException.networkError((IOException) throwable);
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.unexpectedError(throwable);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public CallAdapter get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }
}