package guru.springframework.spring6reactive.bootstrap;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.repositories.BeerRepository;
import guru.springframework.spring6reactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData()
                .subscribe(null,
                        throwable -> log.error("Error loading beer data", throwable),
                        () -> log.info("Beer data loaded successfully"));

        loadCustomerData()
                .subscribe(null,
                        throwable -> log.error("Error loading Customer data", throwable),
                        () -> log.info("Customer data loaded successfully"));
    }

    private Mono<Void> loadBeerData() {
        return beerRepository.count()
                .filter(count -> count == 0)
                .flatMapMany(count -> {
                    Beer beer1 = Beer.builder()
                            .beerName("Galaxy Cat")
                            .beerStyle("Pale Ale")
                            .upc("123456")
                            .price(new BigDecimal("12.99"))
                            .quantityOnHand(122)
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();

                    Beer beer2 = Beer.builder()
                            .beerName("Crank")
                            .beerStyle("Pale Ale")
                            .upc("1235622")
                            .price(new BigDecimal("11.99"))
                            .quantityOnHand(392)
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();

                    Beer beer3 = Beer.builder()
                            .beerName("Sunshine City")
                            .beerStyle("IPA")
                            .upc("123456")
                            .price(new BigDecimal("13.99"))
                            .quantityOnHand(144)
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();

                    return beerRepository.saveAll(List.of(beer1, beer2, beer3));

                }).then();

    }

    private Mono<Void> loadCustomerData() {
        return customerRepository.count()
                .filter(count -> count == 0)
                .flatMapMany(count -> {
                    Customer customer1 = Customer.builder()
                            .customerName("Alice")
                            .build();

                    Customer customer2 = Customer.builder()
                            .customerName("Bob")
                            .build();

                    Customer customer3 = Customer.builder()
                            .customerName("Charlie")
                            .build();

                    return customerRepository.saveAll(List.of(customer1, customer2, customer3));
                }).then();

    }
}
