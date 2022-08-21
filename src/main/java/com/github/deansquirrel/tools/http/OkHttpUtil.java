package com.github.deansquirrel.tools.http;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static volatile OkHttpClient okHttpClient = null;

    public static synchronized OkHttpClient getOkHttpClient() {
        if(okHttpClient != null) {
            return okHttpClient;
        }
        synchronized (OkHttpUtil.class) {
            if(okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory(),x509TrustManager())
                        .retryOnConnectionFailure(false)
                        .connectionPool(pool())
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .hostnameVerifier((hostname, session) -> true)
//                        .cookieJar(new CookieJar() {
//                            @Override
//                            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                                getCookieStore().put(url.host(), cookies);
//                            }
//                            @Override
//                            public List<Cookie> loadForRequest(HttpUrl url) {
//                                List<Cookie> cookies = getCookieStore().get(url.host());
//                                return cookies != null ? cookies : new ArrayList<Cookie>();
//                            }
//                        })
//                        设置代理
//                        .proxy(proxy)
//                        拦截器
//                        .addInterceptor(interceptor)
                        .build();

            }
        }
        return okHttpClient;
    }

    private static  X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory sslSocketFactory() {
        try{
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static ConnectionPool pool() {
        return new ConnectionPool(5,3,TimeUnit.SECONDS);
    }

}
