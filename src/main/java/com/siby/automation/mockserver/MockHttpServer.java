package com.siby.automation.mockserver;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Header;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockHttpServer {

    private MockServerClient client;

    public MockHttpServer() {
    }

    public MockHttpServer(String host, int port) {
        client = new MockServerClient(host, port);
    }

    public void stubResponseJson(String path, String responseJson) throws Throwable {
        stubResponseJson(path, responseJson, 200);
    }

    public void stubResponseJson(String path, String responseJson, int statusCode) throws Throwable {
        client.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(responseJson)
                );
    }
}
