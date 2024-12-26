package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {
    public static final String BEERS_PATH = "/api/v2/beer";
    public static final String BEERS_PATH_ID = BEERS_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEERS_PATH)
    Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEERS_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.findById(beerId);
    }

    @PostMapping(BEERS_PATH)
    Mono<ResponseEntity<Void>> createBeer(@RequestBody BeerDTO beerDTO) {
        return beerService.createBeer(beerDTO)
                .map(savedDto -> ResponseEntity.created(UriComponentsBuilder
                        .fromHttpUrl("http://localhost:8080" + BEERS_PATH + "/" + savedDto.getId())
                        .build().toUri()).build());
    }

    @PutMapping(BEERS_PATH_ID)
    Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer beerId, @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(beerId, beerDTO)
                .map(savedDto -> ResponseEntity.noContent().build());
    }

    @PatchMapping(BEERS_PATH_ID)
    Mono<ResponseEntity<Void>> patchBeer(@PathVariable("beerId") Integer beerId, @RequestBody BeerDTO beerDTO) {
        return beerService.patchBeer(beerId, beerDTO)
                .map(savedDto -> ResponseEntity.noContent().build());
    }
}
