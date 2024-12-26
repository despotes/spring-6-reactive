package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.model.BeerDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.channels.FileChannel;

public interface BeerService {
    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> findById(Integer beerId);

    Mono<BeerDTO> createBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO);

    Mono<Void> deleteBeer(Integer beerId);
}
