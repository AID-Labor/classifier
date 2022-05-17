module okhttp3 {
    requires java.logging;

    requires transitive okio;

    exports okhttp3;
    exports okhttp3.internal;
    exports okhttp3.internal.annotations;
    exports okhttp3.internal.cache;
    exports okhttp3.internal.cache2;
    exports okhttp3.internal.connection;
    exports okhttp3.internal.http;
    exports okhttp3.internal.http1;
    exports okhttp3.internal.http2;
    exports okhttp3.internal.io;
    exports okhttp3.internal.platform;
    exports okhttp3.internal.proxy;
    exports okhttp3.internal.publicsuffix;
    exports okhttp3.internal.tls;
    exports okhttp3.internal.ws;

}
