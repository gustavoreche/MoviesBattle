package com.br.MoviesBattle.service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.br.MoviesBattle.client.ImdbClient;
import com.br.MoviesBattle.model.database.UserAction;
import com.br.MoviesBattle.model.imdb.ImdbMovieDTO;
import com.br.MoviesBattle.model.imdb.ImdbTop250MoviesResponse;
import com.br.MoviesBattle.model.imdb.ImdbTwoMoviesResponse;
import com.br.MoviesBattle.model.user.UserDTO;
import com.br.MoviesBattle.repository.UserActionRepository;

@Service
public class ImdbService {
	
	private ImdbClient imdbClient;
	private UserActionRepository userActionRepository;
	private ImdbTop250MoviesResponse movies;

	public ImdbService(ImdbClient imdbClient,
			UserActionRepository userActionRepository) {
		this.imdbClient = imdbClient;
		this.userActionRepository = userActionRepository;
	}
	
	public void setMovies() {
		this.movies = this.imdbClient.get250Movies();
	}
	
	public ImdbTwoMoviesResponse getTwoMovies(String idGame) {
		List<UserAction> movies = this.userActionRepository.findByIdGame(idGame);
		
		ImdbMovieDTO movie1 = this.movies.getItems().get(new Random().nextInt(249));
		ImdbMovieDTO movie2 = this.movies.getItems().get(new Random().nextInt(249));
		while(movie1.getId() == movie2.getId() 
				|| movie1.getPontuation().doubleValue() == movie2.getPontuation().doubleValue()
				|| isSequenceWithSameMovie(movies, movie1.getId(), movie2.getId())) {
			movie2 = this.movies.getItems().get(new Random().nextInt(249));
		}
		return new ImdbTwoMoviesResponse(movie1, movie2);
	}

	private boolean isSequenceWithSameMovie(List<UserAction> movies,
			String idMovie1,
			String idMovie2) {
		return movies
			.stream()
			.anyMatch(movie -> {
				String idMovieCompared = "";
				if(idMovie1.equalsIgnoreCase(movie.getIdMovie1())) {
					idMovieCompared = movie.getIdMovie2();
				} else if(idMovie1.equalsIgnoreCase(movie.getIdMovie2())){
					idMovieCompared = movie.getIdMovie1();
				}
				return idMovieCompared.equalsIgnoreCase(idMovie1) 
						|| idMovieCompared.equalsIgnoreCase(idMovie2);
			});
	}

	public UserDTO getTwoMoviesResult(String idMovie1, String idMovie2, 
			String idMovieSelected, String idGame, 
			Authentication authentication) {
		ImdbMovieDTO movie1 = this.movies.getMovieById(idMovie1);
		ImdbMovieDTO movie2 = this.movies.getMovieById(idMovie2);
		ImdbMovieDTO bestMovie = movie1.getPontuation() > movie2.getPontuation() 
				? movie1 
				: movie2;
		ImdbMovieDTO movieSelected = movie1.getId().equals(idMovieSelected)
				? movie1 
				: movie2;
				
		System.out.println("Filme selecionado: " + movieSelected.getPontuation().doubleValue());
		System.out.println("Melhor filme: " + bestMovie.getPontuation().doubleValue());
		boolean correctChoice = movieSelected.getPontuation().doubleValue() == bestMovie.getPontuation().doubleValue();
		int point = correctChoice ? 1 : 0;
		int wrongChoice = correctChoice ? 0 : 1;
		
		UserAction userAction = new UserAction(idGame, authentication.getName(), 
				idMovie1, idMovie2, point, wrongChoice, getCountWrongChoice(idGame, wrongChoice) >= 3);
		this.userActionRepository.save(userAction);
		
		return new UserDTO(userAction);
	}

	private Integer getCountWrongChoice(String idGame, Integer wrongChoice) {
		Integer countWrongChoice = this.userActionRepository.countWrongChoice(idGame);
		return Objects.isNull(countWrongChoice)
				? 0
				: countWrongChoice + wrongChoice;
	}
	
}
