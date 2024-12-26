package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.CustomerDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    CustomerDTO getCustomerTest() {
        return CustomerDTO.builder()
                .customerName("Test").build();
    }

    private CustomerDTO getCustomerDTOBadRequest() {
        CustomerDTO customerDTO = getCustomerTest();
        customerDTO.setCustomerName("");
        return customerDTO;
    }

    @Test
    @Order(1)
    void testListCustomers() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(2)
    void testGetById() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testCreateCustomer() {
        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(getCustomerTest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }

    @Test
    @Order(3)
    void testCreateCustomerBadRequest() {
        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(getCustomerDTOBadRequest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(4)
    void testUpdateCustomer() {
        webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getCustomerTest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(4)
    void testUpdateCustomerNotFound() {
        webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getCustomerTest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testUpdateCustomerBadRequest() {
        webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getCustomerDTOBadRequest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(4)
    void testPatchCustomer() {
        webTestClient.patch().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getCustomerTest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchCustomerNotFound() {
        webTestClient.patch().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getCustomerTest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testPatchCustomerBadRequest() {
        webTestClient.patch().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getCustomerDTOBadRequest()), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(5)
    void testDeleteCustomer() {
        webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    void testDeleteCustomerNotFound() {
        webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }
}