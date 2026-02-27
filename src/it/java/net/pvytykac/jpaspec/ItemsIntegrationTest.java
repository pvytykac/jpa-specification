package net.pvytykac.jpaspec;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = PostgresContainerInitializer.class)
class ItemsIntegrationTest {

    @Test
    void createItemAndListWithFilter(@Autowired WebTestClient client) throws Exception {
        var payload = new JSONObject()
                .put("name", "Juice")
                .put("price", 24.99D)
                .put("status", "PENDING");

        client.post()
                .uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Juice")
                .jsonPath("$.price").isEqualTo(24.99D)
                .jsonPath("$.status").isEqualTo("PENDING");

        client.get()
                .uri(builder -> builder.path("/v1/items")
                        .queryParam("name", "uic")
                        .queryParam("nameOperator", "CONTAINS")
                        .queryParam("status", "PENDING", "ACTIVE", "DISABLED")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content[0].name").isEqualTo("Juice")
                .jsonPath("$.content[0].price").isEqualTo(24.99D)
                .jsonPath("$.content[0].status").isEqualTo("PENDING");
    }

    @Test
    void createItemAndDelete(@Autowired WebTestClient client) throws Exception {
        var payload = new JSONObject()
                .put("name", "Juice")
                .put("price", 24.99D)
                .put("status", "PENDING");

        var body = client.post()
                .uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Juice")
                .jsonPath("$.price").isEqualTo(24.99D)
                .jsonPath("$.status").isEqualTo("PENDING")
                .returnResult().getResponseBody();

        assertThat(body).isNotNull();
        var itemId = new JSONObject(new String(body, Charset.defaultCharset())).getString("id");

        client.get()
                .uri(builder -> builder.path("/v1/items/{itemId}")
                        .build(itemId))
                .exchange()
                .expectStatus().isOk();

        client.delete()
                .uri(builder -> builder.path("/v1/items/{itemId}")
                        .build(itemId))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Juice")
                .jsonPath("$.price").isEqualTo(24.99D)
                .jsonPath("$.status").isEqualTo("PENDING");

        client.get()
                .uri(builder -> builder.path("/v1/items/{itemId}")
                        .build(itemId))
                .exchange()
                .expectStatus().isNotFound();
    }

}
