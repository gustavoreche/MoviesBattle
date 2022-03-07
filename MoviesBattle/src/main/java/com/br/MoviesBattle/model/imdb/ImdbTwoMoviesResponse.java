package com.br.MoviesBattle.model.imdb;

public class ImdbTwoMoviesResponse {

	private ImdbMovieDTO movie1;
	private ImdbMovieDTO movie2;
	
	public ImdbTwoMoviesResponse(ImdbMovieDTO movie1, ImdbMovieDTO movie2) {
		this.movie1 = movie1;
		this.movie2 = movie2;
	}

	public ImdbMovieDTO getMovie1() {
		return movie1;
	}

	public ImdbMovieDTO getMovie2() {
		return movie2;
	}

}
