package com.knoema.core;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.entity.NStringEntity;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ClientCore {

    final private static ObjectMapper JSON_MAPPER = createDefaultMapper();
    final private static String AUTH_PROTO_VERSION = "1.2";
    final protected static int DEFAULT_TIMEOUT = 10 * 60 * 1000;
    final private CloseableHttpAsyncClient httpclient;
    final private String host;
    final private String token;
    final private String clientId;
    final private String clientSecret;
    final static private SimpleDateFormat AUTH_DATE_FORMAT = Utils.getUTCDateFormat("dd-MM-yy-HH");

    protected ClientCore(String host, String token, String clientId, String clientSecret) {
        this.host = host;
        this.token = token;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.httpclient = buildClient(DEFAULT_TIMEOUT)
                .build();
    }

    protected ClientCore(String host, ClientCore source) {
        this(host, source.token, source.clientId, source.clientSecret);
    }

    public static ObjectMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    public String getHost() {
        return host;
    }

    protected static HttpAsyncClientBuilder buildClient(int timeout) {
        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();

        return HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig);
    }

    protected void close() {
        try {
            this.httpclient.close();
        }
        catch (final IOException e) {
            logUnexpectedError(e);
        }
    }

    private String createClientAuthorizationHeader() {
        String dateStr = AUTH_DATE_FORMAT.format(new Date());

        String authStr;
        try {
            authStr = DatatypeConverter.printBase64Binary(
                    new HmacUtils(HmacAlgorithms.HMAC_SHA_1, dateStr.getBytes("utf-8")).hmac(clientSecret.getBytes("utf-8")));
        }
        catch (final UnsupportedEncodingException e) {
            logUnexpectedError(e);
            return "";
        }

        return String.format("Knoema %s:%s:%s", clientId, authStr, AUTH_PROTO_VERSION);
    }

    private <T> RequestExecutor<T> execute(HttpUriRequest request, JsonResponseConsumer<T> consumer) {
        if (clientId != null && clientSecret != null) {
            request.setHeader("Authorization", createClientAuthorizationHeader());
        }
        return new RequestExecutor<>(request, consumer, httpclient);
    }

    protected static JavaType ConstructListType(Class<?> itemType) {
        return TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, itemType);
    }

    protected static JavaType ConstructClassType(Class<?> type) {
        return TypeFactory.defaultInstance().constructType(type);
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    static private <T> JsonResponseConsumer<T> createConsumer(JavaType responseType) {
        return new JsonResponseConsumer<>(JSON_MAPPER, responseType);
    }

    private URI createURI(String path, Iterable<Map.Entry<String, String>> params) {
        final URIBuilder b = new URIBuilder();
        b.setScheme("http");
        b.setHost(host);
        if (path != null) {
            b.setPath(path);
        }
        if (token != null) {
            b.setParameter("access_token", token);
        }
        if (clientId != null && clientSecret == null) {
            b.setParameter("client_id", clientId);
        }

        if (params != null) {
            for (Map.Entry<String, String> param : params) {
                b.setParameter(param.getKey(), param.getValue());
            }
        }

        try {
            return b.build();
        }
        catch (final URISyntaxException e) {
            logUnexpectedError(e);
        }
        return null;
    }

    private HttpGet createGet(String path, Iterable<Map.Entry<String, String>> params) {
        return new HttpGet(createURI(path, params));
    }

    private void logUnexpectedError(Exception e) {
        e.printStackTrace();
    }

    private String createContentString(Object request) {
        final ObjectMapper objectMapper = getJsonMapper();
        final StringWriter sw = new StringWriter();
        try {
            objectMapper.writeValue(sw, request);
        }
        catch (final IOException e) {
            logUnexpectedError(e);
        }
        return sw.toString();
    }

    private HttpPost createPost(String path, Object request) {
        final HttpPost post = new HttpPost(createURI(path, null));
        if (request != null) {
            final HttpEntity entity = (request instanceof HttpEntity) ? (HttpEntity)request :
                new NStringEntity(createContentString(request), ContentType.APPLICATION_JSON);
            post.setEntity(entity);
        }
        return post;
    }

    protected <Resp> RequestExecutor<Resp> apiGet(String path, Iterable<Map.Entry<String, String>> params, JavaType responseType) {
        final JsonResponseConsumer<Resp> consumer = createConsumer(responseType);
        return execute(createGet(path, params), consumer);
    }

    protected <Resp> RequestExecutor<Resp> apiGet(String path, JavaType responseType) {
        return apiGet(path, null, responseType);
    }

    protected <Resp> RequestExecutor<Resp> apiPost(String path, Object request, JavaType responseType) {
        final JsonResponseConsumer<Resp> consumer = createConsumer( responseType);
        return execute(createPost(path, request), consumer);
    }
}
