package com.br.MoviesBattle.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.MoviesBattle.model.imdb.ImdbTop250MoviesResponse;
import com.br.MoviesBattle.service.ImdbService;

@RestController
@RequestMapping("/movie")
public class MovieResource {
	
	private ImdbService imdbService;
	
	public MovieResource(ImdbService imdbService) {
		this.imdbService = imdbService;
	}
	
	@GetMapping("/top-movies")
	public ImdbTop250MoviesResponse start() {
		return this.imdbService.getTop250Movies();
	}
	
}
