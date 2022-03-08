package com.br.MoviesBattle.model.imdb;

import java.util.List;

public class ImdbTop250MoviesResponse {
	
	public ImdbTop250MoviesResponse() {
	}
	
	public ImdbTop250MoviesResponse(List<ImdbMovieDTO> items) {
		this.items = items;
	}

	private List<ImdbMovieDTO> items;

	public List<ImdbMovieDTO> getItems() {
		return items;
	}
	
	public ImdbMovieDTO getMovieById(String idMovie) {
		return this.items
			.stream()
			.filter(movie -> movie.getId().equalsIgnoreCase(idMovie))
			.findFirst()
			.orElseGet(() -> new ImdbMovieDTO());
	}

}
