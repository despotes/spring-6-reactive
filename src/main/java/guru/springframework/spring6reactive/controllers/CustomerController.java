package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.CustomerDTO;
import guru.springframework.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO)
                .map(savedDto -> ResponseEntity.created(UriComponentsBuilder
                        .fromHttpUrl("http://localhost:8080" + CUSTOMER_PATH + "/" + savedDto.getId())
                        .build().toUri()).build());
    }

    @PutMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> updateCustomer(@PathVariable("customerId") Integer customerId, @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(customerId, customerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(savedDto -> ResponseEntity.noContent().build());
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchCustomer(@PathVariable("customerId") Integer customerId, @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.patchCustomer(customerId, customerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(savedDto -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable("customerId") Integer customerId) {
        return customerService
                .getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(foundCustomer -> customerService.deleteCustomer(foundCustomer.getId()))
                .thenReturn(ResponseEntity.noContent().build());
    }
}
