package com.feelsokman.net.net.error;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Taken from https://gist.github.com/koesie10/bc6c62520401cc7c858f#file-retrofitexception-java
 */
public class RetrofitException extends RuntimeException {

    private final String url;
    private final Response response;
    private final Kind kind;
    private final Integer code;
    private final Retrofit retrofit;

    @SuppressWarnings("WeakerAccess")
    RetrofitException(String message,
                      String url,
                      Response response,
                      Kind kind,
                      Integer code,
                      Throwable exception,
                      Retrofit retrofit) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        this.code = code;
        this.retrofit = retrofit;
    }

    public static RetrofitException httpError(String url, Response response, Retrofit retrofit) {
        String message = response.code() + " " + response.message();
        return new RetrofitException(message, url, response, Kind.HTTP, response.code(), null, retrofit);
    }

    public static RetrofitException networkError(IOException exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, null, exception, null);
    }

    public static RetrofitException unexpectedError(Throwable exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.UNEXPECTED, null, exception, null);
    }

    private static String getBodyAsString(ResponseBody responseBody) {
        if (responseBody == null) {
            return null;
        } else {
            try {
                return responseBody.string();
            } catch (IOException | IllegalStateException e) {
                return null;
            }
        }
    }

    /**
     * Identifies the event kind which triggered a {@link RetrofitException}.
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * The event kind which triggered this error.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * The Retrofit this request was executed on
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     */
    @SuppressWarnings("WeakerAccess")
    public <T> T getErrorBodyAs(Class<T> type) throws IOException, IllegalStateException {

        if (response == null || response.errorBody() == null) {
            return null;
        } else {
            Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);
            return converter.convert(response.errorBody());
        }
    }

    public RestErrorBody getDeserialisedError() {
        try {
            return getErrorBodyAs(RestErrorBody.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getErrorBody() {
        if (response == null || response.errorBody() == null) {
            return null;
        } else {
            return getBodyAsString(response.errorBody());
        }
    }
}