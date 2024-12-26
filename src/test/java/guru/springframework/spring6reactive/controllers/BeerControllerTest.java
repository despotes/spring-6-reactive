package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(2)
    void testListBeers() {
        webTestClient.get().uri(BeerController.BEERS_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(1)
    void testGetById() {
        webTestClient.get().uri(BeerController.BEERS_PATH_ID ,+ 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    @Order(3)
    void testCreateBeer() {
        webTestClient.post().uri(BeerController.BEERS_PATH)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    @Order(4)
    void testUpdateBeer() {
        webTestClient.put().uri(BeerController.BEERS_PATH_ID, 1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    void testDeleteBeer() {
        webTestClient.delete().uri(BeerController.BEERS_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }
}