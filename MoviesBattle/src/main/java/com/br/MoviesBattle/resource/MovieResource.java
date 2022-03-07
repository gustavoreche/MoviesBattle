package com.br.MoviesBattle.resource;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.MoviesBattle.model.imdb.ImdbTwoMoviesResponse;
import com.br.MoviesBattle.model.user.UserDTO;
import com.br.MoviesBattle.service.ImdbService;

@RestController
public class MovieResource {
	
	public static final String MOVIE_TWO_MOVIES = "/movie/two-movies";
	public static final String MOVIE_TWO_MOVIES_RESULT = "/movie/two-movies/result";
	
	private ImdbService imdbService;
	
	public MovieResource(ImdbService imdbService) {
		this.imdbService = imdbService;
	}
	
	@GetMapping(MOVIE_TWO_MOVIES)
	public ImdbTwoMoviesResponse getTwoMovies() {
		return this.imdbService.getTwoMovies();
	}
	
	@GetMapping(MOVIE_TWO_MOVIES_RESULT)
	public UserDTO getTwoMoviesResult(@RequestParam("idMovie1") String idMovie1,
			@RequestParam("idMovie2") String idMovie2,
			@RequestParam("idMovieSelect") String idMovieSelected,
			@RequestParam("idGame") String idGame,
			Authentication authentication) {
		return this.imdbService.getTwoMoviesResult(idMovie1, idMovie2, idMovieSelected, idGame,
				authentication);
	}
	
}
