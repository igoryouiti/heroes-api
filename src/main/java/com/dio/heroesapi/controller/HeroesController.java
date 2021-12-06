package com.dio.heroesapi.controller;

import com.dio.heroesapi.document.Heroes;
import com.dio.heroesapi.repository.HeroesRepository;
import com.dio.heroesapi.service.HeroesService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.dio.heroesapi.constants.HeroesConstant.HEROES_ENDPOINT_LOCAL;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class HeroesController {

	HeroesService heroService;
	HeroesRepository heroRepository;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HeroesController.class);

	public HeroesController(HeroesService heroService, HeroesRepository heroRepository) {
		this.heroService = heroService;
		this.heroRepository = heroRepository;
	}

	@GetMapping(HEROES_ENDPOINT_LOCAL)
	public Flux<Heroes> getAllItems() {
		log.info("Requesting list of all heroes");
		return heroService.findAll();
	}

	@GetMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
	public Mono<ResponseEntity<Heroes>> findById(@PathVariable String id) {
		log.info("Requesting hero with id{}", id);
		return heroService.findByIdHero(id).map((item) -> 
			new ResponseEntity<>(item, HttpStatus.OK))
			.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping(HEROES_ENDPOINT_LOCAL)
	@ResponseStatus(code=HttpStatus.CREATED)
	public Mono<Heroes> createHero(@RequestBody Heroes hero){
		log.info("A new Hero was created");
		return heroService.save(hero);
	}
	
	@DeleteMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	public Mono<HttpStatus> deleteById(@PathVariable String id){	
		heroService.deleteByIdHero(id);
		log.info("Deleting a hero with id {}", id);
		return Mono.just(HttpStatus.NOT_FOUND);
	}
}
