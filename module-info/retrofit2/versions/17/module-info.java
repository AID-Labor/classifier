module retrofit2 {
    requires okio;

    requires transitive okhttp3;

    exports retrofit2;
    exports retrofit2.http;
    exports retrofit2.internal;

}
