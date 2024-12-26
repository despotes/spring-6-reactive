package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDTO> listCustomers();
    Mono<CustomerDTO> getCustomerById(Integer id);
    Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO);
    Mono<CustomerDTO> updateCustomer(Integer id, CustomerDTO customerDTO);
    Mono<CustomerDTO> patchCustomer(Integer id, CustomerDTO customerDTO);
    Mono<Void> deleteCustomer(Integer id);
}
