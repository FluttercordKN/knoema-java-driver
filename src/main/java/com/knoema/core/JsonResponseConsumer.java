package com.knoema.core;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.CharBuffer;

class JsonResponseConsumer<T> extends AsyncCharConsumer<T> {

    final private ObjectMapper jsonMapper;
    final private JavaType resultType;
    final private StringWriter buffer;
    private int status;

    JsonResponseConsumer(ObjectMapper jsonMapper, JavaType resultType) {
        this.jsonMapper = jsonMapper;
        this.resultType = resultType;
        this.buffer = new StringWriter();
        this.status = 500;
    }

    protected void onCharReceived(CharBuffer charBuffer, IOControl ioControl) {
        buffer.append(charBuffer);
    }

    protected void onResponseReceived(HttpResponse httpResponse) {
        status = httpResponse.getStatusLine().getStatusCode();
    }

    protected T buildResult(HttpContext httpContext) throws HttpException, IOException {
        if (status < 200 || status >= 300) {
            throw new HttpException(String.valueOf(status));
        }
        final StringReader reader = new StringReader(buffer.toString());
        return jsonMapper.readValue(reader, resultType);
    }
}
