package com.damonallison.libraries.okhttp;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OkHttp is a modern, simple, efficient HTTP client.
 * https://square.github.io/okhttp/
 */
public class OkHttpTests {

    private static final Logger LOGGER = Logger.getLogger(OkHttpTests.class.getName());
    /**
     * OkHttpClients should be shared. OkHttpClient shares connection and thread pools
     * across requests.
     */
    private OkHttpClient client;
    private static final String RESPONSE_STRING = "{ \"name\" : \"damon\" }";

    @BeforeEach
    public void beforeAll() {
        // Use a .Builder() if you want to customize the default instance.
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor())
                .build();
    }

    @Test
    public void testSimpleRequest() throws IOException {

        Request request = new Request.Builder()
                .url("https://google.com")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.isSuccessful());
            assertEquals(RESPONSE_STRING, response.body().string());
        }
    }

    /**
     * MockInterceptor will intercept the request and return a mocked response.
     */
    private class MockInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {


            return chain.proceed(chain.request()).newBuilder()
                    .code(200)
                    .protocol(Protocol.HTTP_2)
                    .message(RESPONSE_STRING)
                    .body(ResponseBody.create(RESPONSE_STRING, MediaType.parse("application/json")))
                    .addHeader("content-type", "application/json")
                    .build();

        }
    }
}
