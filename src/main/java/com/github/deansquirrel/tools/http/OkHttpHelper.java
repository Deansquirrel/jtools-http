package com.github.deansquirrel.tools.http;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OkHttpHelper {

    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    private static final MediaType FORM_DATA = MediaType.parse("multipart/form-data");
    private static final MediaType FILE = MediaType.parse("application/octet-stream");

    private static final Logger logger = LoggerFactory.getLogger(OkHttpHelper.class);

    private OkHttpHelper(){}

    private static OkHttpClient getOkHttpClient() {
        return OkHttpUtil.getOkHttpClient();
    }

    private static Request getGetRequest(String url, Map<String, String> headers, Map<String, String> params) {
        Request request = null;
        if(params != null && !params.isEmpty()) {
            HttpUrl httpUrl = HttpUrl.parse(url);
            if(httpUrl != null) {
                httpUrl = addQueryParameter(httpUrl, params);
                request = getRequest(httpUrl, METHOD_GET, getHeaders(headers), null);
            }
        }
        if(request == null) {
            request = getRequest(url, METHOD_GET, getHeaders(headers), null);
        }
        return request;
    }

    public static String doGet(String url) throws IOException {
        return doGet(url, null, null);
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, null, params);
    }

    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        return execute(getGetRequest(url, headers, params));
    }

    public static void doGetAsync(String url, ICallback callback) {
        doGetAsync(url, null, null, callback);
    }

    public static void doGetAsync(String url, Map<String, String> params, ICallback callback) {
        doGetAsync(url, null, params, callback);
    }

    public static void doGetAsync(String url, Map<String, String> headers, Map<String, String> params,
                                   ICallback callback) {
        execute(getGetRequest(url, headers, params), callback);
    }

    public static String doPost(String url, MediaType contentType, String content) throws IOException {
        return doPost(url, null, contentType, content);
    }

    public static String doPost(String url, Map<String, String> headers,
                                MediaType contentType, String content) throws IOException {
        return execute(getRequest(url, METHOD_POST, getHeaders(headers),
                getRequestBody(contentType, content)));
    }

    public static void doPostAsync(String url, MediaType contentType, String content, ICallback callback) {
        doPostAsync(url, null, contentType, content, callback);
    }

    public static void doPostAsync(String url, Map<String, String> headers,
                                   MediaType contentType, String content, ICallback callback) {
        execute(getRequest(url, METHOD_POST, getHeaders(headers),
                getRequestBody(contentType, content)), callback);
    }

    public static String doPostJson(String url, String content) throws IOException {
        return doPost(url, JSON, content);
    }

    public static String doPostJson(String url, Map<String, String> headers, String content) throws IOException {
        return doPost(url, headers, JSON, content);
    }

    public static void doPostJsonAsync(String url, String content, ICallback callback) {
        doPostAsync(url, JSON, content, callback);
    }

    public static void doPostJsonAsync(String url, Map<String, String> headers, String content, ICallback callback) {
        doPostAsync(url, headers, JSON, content, callback);
    }

    public static HttpUrl addPathSegment(HttpUrl httpUrl, String pathSegment) {
        return httpUrl.newBuilder().addPathSegment(pathSegment).build();
    }

    public static HttpUrl addPathSegment(HttpUrl httpUrl, List<String> pathSegment) {
        HttpUrl.Builder builder = httpUrl.newBuilder();
        if(pathSegment != null) {
            for(String p : pathSegment) {
                builder.addPathSegment(p);
            }
        }
        return builder.build();
    }

    public static HttpUrl addEncodedPathSegment(HttpUrl httpUrl, String pathSegment) {
        return httpUrl.newBuilder().addEncodedPathSegment(pathSegment).build();
    }

    public static HttpUrl addEncodedPathSegment(HttpUrl httpUrl, List<String> pathSegment) {
        HttpUrl.Builder builder = httpUrl.newBuilder();
        if(pathSegment != null) {
            for(String p : pathSegment) {
                builder.addEncodedPathSegment(p);
            }
        }
        return builder.build();
    }

    public static HttpUrl addQueryParameter(HttpUrl httpUrl, String name, String value) {
        return httpUrl.newBuilder().addQueryParameter(name,value).build();
    }

    public static HttpUrl addQueryParameter(HttpUrl httpUrl, Map<String, String> queryParameter) {
        HttpUrl.Builder builder = httpUrl.newBuilder();
        if(queryParameter != null) {
            for(String p : queryParameter.keySet()) {
                builder.addQueryParameter(p, queryParameter.get(p));
            }
        }
        return builder.build();
    }

    public static HttpUrl addEncodedQueryParameter(HttpUrl httpUrl, String name, String value) {
        return httpUrl.newBuilder().addEncodedQueryParameter(name,value).build();
    }

    public static HttpUrl addEncodedQueryParameter(HttpUrl httpUrl, Map<String, String> encodedQueryParameter) {
        HttpUrl.Builder builder = httpUrl.newBuilder();
        if(encodedQueryParameter != null) {
            for(String p : encodedQueryParameter.keySet()) {
                builder.addQueryParameter(p, encodedQueryParameter.get(p));
            }
        }
        return builder.build();
    }

    public static HttpUrl getHttpUrl(String scheme, String host){
        return getHttpUrl(scheme, host, null, null, null);
    }

    public static HttpUrl getHttpUrl(String scheme, String host,List<String> pathSegment){
        return getHttpUrl(scheme, host, pathSegment, null, null);
    }

    public static HttpUrl getHttpUrl(String scheme, String host,
                                 List<String> pathSegment,
                                 Map<String, String> queryParameter){
        return getHttpUrl(scheme, host, pathSegment, queryParameter, null);
    }

    public static HttpUrl getHttpUrl(String scheme, String host,
                                 List<String> pathSegment,
                                 Map<String, String> queryParameter,
                                 Map<String, String> encodedQueryParameter){
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host);
        if(pathSegment != null) {
            for(String p : pathSegment) {
                builder.addPathSegment(p);
            }
        }
        if(queryParameter != null) {
            for(String p : queryParameter.keySet()) {
                builder.addQueryParameter(p, queryParameter.get(p));
            }
        }
        if(encodedQueryParameter != null) {
            for(String p : encodedQueryParameter.keySet()) {
                builder.addEncodedQueryParameter(p, encodedQueryParameter.get(p));
            }
        }
        return builder.build();
    }

    public static Headers getHeaders(Headers headers, String name, String value) {
        return headers.newBuilder().add(name, value).build();
    }

    public static Headers getHeaders(Headers headers, Map<String, String> headersMap) {
        return headers.newBuilder().addAll(getHeaders(headersMap)).build();
    }

    public static Headers getHeaders(Headers headers, String... add) {
        if(add.length < 1) {
            return headers;
        }
        Headers addHeaders = getHeaders(add);
        if(addHeaders == null) {
            return headers;
        }
        return headers.newBuilder().addAll(addHeaders).build();
    }

    public static Headers getHeaders(String... headers) {
        if(headers.length < 1) {
            return null;
        }
        return Headers.of(headers);
    }

    public static Headers getHeaders(Map<String, String> headersMap) {
        if(headersMap == null) {
            return null;
        }
        return Headers.of(headersMap);
    }

    public static Headers getHeaders(Headers headers) {
        if(headers == null) {
            return null;
        }
        return new Headers.Builder().addAll(headers).build();
    }

    public static RequestBody getRequestBody(MediaType contentType, String content) {
        return RequestBody.Companion.create(content, contentType);
    }

    public static RequestBody getRequestBodyJSON(String content) {
        return RequestBody.Companion.create(content, JSON);
    }

    public static RequestBody getRequestBodyXML(String content) {
        return RequestBody.Companion.create(content, XML);
    }

    public static RequestBody getRequestBody(File file) {
        return RequestBody.Companion.create(file, FILE);
    }

    public static FormBody getRequestBody(Map<String, String> map) {
        return getFormBody(map, null);
    }

    public static FormBody getFormBody(Map<String, String> map, Map<String, String> encodedMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if(map != null) {
            for(String k:map.keySet()) {
                builder.add(k, map.get(k));
            }
        }
        if(encodedMap != null) {
            for(String k:encodedMap.keySet()) {
                String v = encodedMap.get(k);
                if(v != null) {
                    builder.addEncoded(k, v);
                }
            }
        }
        return builder.build();
    }

    public static MultipartBody getMultipartBody(Map<String, String> dataPart, Map<String, File> filePart) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if(FORM_DATA != null) {
            builder.setType(OkHttpHelper.FORM_DATA);
        }
        if(dataPart != null) {
            for(String k : dataPart.keySet()) {
                builder.addFormDataPart(k, dataPart.get(k));
            }
        }
        if(filePart != null) {
            for(String k : filePart.keySet()) {
                File f = filePart.get(k);
                if(k != null) {
                    builder.addFormDataPart(k, f.getName(), getRequestBody(f));
                }
            }
        }
        return builder.build();
    }

    public static Request getRequest(HttpUrl url, String method, Headers headers, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder().url(url);
        if(headers != null) {
            builder = builder.headers(headers);
        }
        if(requestBody != null) {
            builder = builder.method(method, requestBody);
        }
        return builder.build();
    }

    public static Request getRequest(String url, String method, Headers headers, RequestBody requestBody) {
        return getRequest(HttpUrl.get(url), method, headers, requestBody);
    }

    public static String execute(Request request) throws IOException {
        try (Response response = getOkHttpClient().newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        }
        throw new IOException("返回数据为空");
    }

    public static void execute(Request request, ICallback callback) {
        if(request == null) {
            logger.warn("request is null");
            return;
        }

        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.body() != null) {
                    try {
                        if(callback != null) {
                            callback.onResponse(call, response.body().string());
                        }
                    } catch (IOException e) {
                        onFailure(call, e);
                    }
                } else {
                    onFailure(call, new IOException("返回数据为空"));
                }
                response.close();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(callback != null) {
                    callback.onFailure(call, e);
                }
            }
        });
    }

}
