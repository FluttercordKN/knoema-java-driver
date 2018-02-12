package com.knoema.core;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RequestExecutor<T> {
    private final HttpUriRequest request;
    private final HttpAsyncResponseConsumer<T> consumer;
    private FutureCallback<T> callback;
    private CloseableHttpAsyncClient httpclient;
    private boolean autoClose;

    public RequestExecutor(HttpUriRequest request, HttpAsyncResponseConsumer<T> consumer, CloseableHttpAsyncClient httpclient) {
        this.request = request;
        this.consumer = consumer;
        this.callback = null;
        this.httpclient = httpclient;
        this.autoClose = false;
    }

    public RequestExecutor<T> callback(FutureCallback<T> callback) {
        this.callback = callback;
        return this;
    }

    public RequestExecutor<T> addCallback(FutureCallback<T> callback) {
        return callback(Utils.chainCallbacks(this.callback, callback));
    }

    public RequestExecutor<T> client(CloseableHttpAsyncClient httpclient) {
        this.httpclient = httpclient;
        return this;
    }

    public RequestExecutor<T> autoClose() {
        this.autoClose = true;
        return this;
    }

    public Future<T> executeAsync() {
        httpclient.start();
        return httpclient.execute(HttpAsyncMethods.create(request), consumer, callback);
    }

    public T get() throws InterruptedException, ExecutionException {
        T result;
        if (autoClose) {
            try {
                result = executeAsync().get();
            }
            finally {
                try {
                    httpclient.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            result = executeAsync().get();
        }
        return result;
    }
}
