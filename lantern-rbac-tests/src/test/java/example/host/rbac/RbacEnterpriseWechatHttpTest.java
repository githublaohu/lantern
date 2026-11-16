package example.host.rbac;

import com.alibaba.fastjson.JSONObject;
import com.lamp.lantern.plugins.auth.platform.EnterpriseWechatClient;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/** Exercises the real HTTP transport against loopback; no calls to an external enterprise tenant. */
class RbacEnterpriseWechatHttpTest {

    private HttpServer server;
    private final Transport client = new Transport();

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    @Test
    void transportReadsRealJsonResponse() {
        respond("/ok", 200, "{\"errcode\":0,\"userid\":\"member\"}");
        assertThat(client.read(url("/ok")).getString("userid")).isEqualTo("member");
    }

    @Test
    void httpFailuresDoNotExposeCredentialUrlOrProviderBody() {
        respond("/error", 500, "private-provider-body");
        assertThatThrownBy(() -> client.read(url("/error?corpsecret=private-secret")))
                .isInstanceOf(IllegalStateException.class).hasMessage("Enterprise WeChat request failed")
                .hasMessageNotContaining("private").hasNoCause();
    }

    @Test
    void transportRejectsRedirectsMalformedJsonAndOversizedBodies() {
        AtomicInteger redirected = new AtomicInteger();
        server.createContext("/redirect", exchange -> {
            exchange.getResponseHeaders().add("Location", url("/steal"));
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        });
        server.createContext("/steal", exchange -> {
            redirected.incrementAndGet();
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        });
        respond("/malformed", 200, "<html>private-error</html>");
        respond("/oversized", 200, "a".repeat(65537));
        for (String path : new String[]{"/redirect", "/malformed", "/oversized"}) {
            assertThatThrownBy(() -> client.read(url(path))).isInstanceOf(IllegalStateException.class)
                    .hasMessage("Enterprise WeChat request failed");
        }
        assertThat(redirected).hasValue(0);
    }

    private void respond(String path, int status, String body) {
        server.createContext(path, exchange -> {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, bytes.length);
            try (java.io.OutputStream output = exchange.getResponseBody()) {
                output.write(bytes);
            }
        });
    }

    private String url(String path) {
        return "http://127.0.0.1:" + server.getAddress().getPort() + path;
    }

    private static class Transport extends EnterpriseWechatClient {
        Transport() {
            super(RbacEnterpriseWechatTest.config());
        }

        JSONObject read(String address) {
            return getJson(address);
        }
    }
}
