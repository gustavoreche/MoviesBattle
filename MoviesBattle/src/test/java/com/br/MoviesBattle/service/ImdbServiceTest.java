package com.br.MoviesBattle.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import com.br.MoviesBattle.client.ImdbClient;
import com.br.MoviesBattle.model.database.UserAction;
import com.br.MoviesBattle.model.imdb.ImdbMovieDTO;
import com.br.MoviesBattle.model.imdb.ImdbTop250MoviesResponse;
import com.br.MoviesBattle.model.imdb.ImdbTwoMoviesResponse;
import com.br.MoviesBattle.model.user.UserDTO;
import com.br.MoviesBattle.repository.UserActionRepository;

public class ImdbServiceTest {
	
	@Mock
	private UserActionRepository userActionRepository;
	
	@Mock
	private	ImdbClient imdbClient;
	
	@Mock
	private List<ImdbMovieDTO> moviesMock;
	
	@InjectMocks
	private ImdbService imdbService = new ImdbService(this.imdbClient,
			this.userActionRepository);
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void getTwoMovies() {
		UserAction user1 = new	UserAction("id1", "gustavo", "A", "B", 1, 0, Boolean.FALSE);
		List<UserAction> movies = List.of(user1);
		Mockito.when(this.userActionRepository.findByIdGame("")).thenReturn(movies);
		
		List<ImdbMovieDTO> imdbMovies = new ArrayList<ImdbMovieDTO>();
		for (int i = 0; i < 250; i++) {
			ImdbMovieDTO imdbMovieDTO = new ImdbMovieDTO(String.valueOf(i), i, "title" + i, 
				"fullTitle" + i, 1996, "image"  + i,
				"crew" + i, Double.valueOf(i), Long.valueOf(i));
			imdbMovies.add(imdbMovieDTO);
		}
		ImdbTop250MoviesResponse imdbTop250MoviesResponse = new	ImdbTop250MoviesResponse(imdbMovies);
		Mockito.when(this.imdbClient.get250Movies()).thenReturn(imdbTop250MoviesResponse);
		
		
		this.imdbService.setMovies();
		ImdbTwoMoviesResponse twoMovies = this.imdbService.getTwoMovies("");
		
		twoMovies.getMovie1();
		
		Assertions.assertNotNull(twoMovies.getMovie1());
		Assertions.assertNotNull(twoMovies.getMovie2());
	}
	
	@Test
	public void getTwoMoviesWithMovie1AndMovie2WithSameIdAndSamePontuationAndSameSequence() {
		UserAction user1 = new	UserAction("id1", "gustavo", "A", "C", 1, 0, Boolean.FALSE);
		List<UserAction> movies = List.of(user1);
		System.out.println(movies.get(0).getIdMovie1());
		Mockito.when(this.userActionRepository.findByIdGame("")).thenReturn(movies);
		
		ImdbMovieDTO movie1 = new ImdbMovieDTO("A", 1, "title1", "fullTitle1",
				1996, "image1", "crew1", 10.5, 10L);
		ImdbMovieDTO movie2 = new ImdbMovieDTO("B", 2, "title2", "fullTitle2",
				1996, "image2", "crew2", 10.5, 10L);
		ImdbMovieDTO movie3 = new ImdbMovieDTO("C", 3, "title3", "fullTitle3",
				1996, "image3", "crew3", 12.5, 10L);
		ImdbMovieDTO movie4 = new ImdbMovieDTO("D", 4, "title4", "fullTitle4",
				1996, "image4", "crew4", 13.5, 10L);
		@SuppressWarnings("unchecked")
		List<ImdbMovieDTO> listMovie = Mockito.mock(List.class);
		ImdbTop250MoviesResponse imdbTop250MoviesResponse = Mockito.mock(ImdbTop250MoviesResponse.class);
		Mockito.when(imdbTop250MoviesResponse.getItems()).thenReturn(listMovie);
		Mockito.when(imdbTop250MoviesResponse.getItems().get(Mockito.anyInt()))
			.thenReturn(movie1)
			.thenReturn(movie1)
			.thenReturn(movie2)
			.thenReturn(movie3)
			.thenReturn(movie4);
		Mockito.when(this.imdbClient.get250Movies()).thenReturn(imdbTop250MoviesResponse);
		
		
		this.imdbService.setMovies();
		ImdbTwoMoviesResponse twoMovies = this.imdbService.getTwoMovies("");
		
		Assertions.assertTrue(twoMovies.getMovie1().getId().equals("A"));
		Assertions.assertTrue(twoMovies.getMovie2().getId().equals("D"));
	}
	
	@Test
	public void getTwoMoviesWithMovie1AndMovie2SameSequence2() {
		UserAction user1 = new	UserAction("id1", "gustavo", "A", "C", 1, 0, Boolean.FALSE);
		List<UserAction> movies = List.of(user1);
		System.out.println(movies.get(0).getIdMovie1());
		Mockito.when(this.userActionRepository.findByIdGame("")).thenReturn(movies);
		
		ImdbMovieDTO movie1 = new ImdbMovieDTO("C", 1, "title1", "fullTitle1",
				1996, "image1", "crew1", 10.5, 10L);
		ImdbMovieDTO movie2 = new ImdbMovieDTO("A", 3, "title3", "fullTitle3",
				1996, "image3", "crew3", 12.5, 10L);
		ImdbMovieDTO movie3 = new ImdbMovieDTO("D", 4, "title4", "fullTitle4",
				1996, "image4", "crew4", 13.5, 10L);
		@SuppressWarnings("unchecked")
		List<ImdbMovieDTO> listMovie = Mockito.mock(List.class);
		ImdbTop250MoviesResponse imdbTop250MoviesResponse = Mockito.mock(ImdbTop250MoviesResponse.class);
		Mockito.when(imdbTop250MoviesResponse.getItems()).thenReturn(listMovie);
		Mockito.when(imdbTop250MoviesResponse.getItems().get(Mockito.anyInt()))
			.thenReturn(movie1)
			.thenReturn(movie2)
			.thenReturn(movie3);
		Mockito.when(this.imdbClient.get250Movies()).thenReturn(imdbTop250MoviesResponse);
		
		
		this.imdbService.setMovies();
		ImdbTwoMoviesResponse twoMovies = this.imdbService.getTwoMovies("");
		
		Assertions.assertTrue(twoMovies.getMovie1().getId().equals("C"));
		Assertions.assertTrue(twoMovies.getMovie2().getId().equals("D"));
	}
	
	@Test
	public void getTwoMoviesResultChoiceWrong() {
		Mockito.when(this.userActionRepository.countWrongChoice("id1")).thenReturn(2);
		Mockito.when(this.userActionRepository.save(Mockito.any(UserAction.class))).thenReturn(null);
		
		ImdbMovieDTO movie1 = new ImdbMovieDTO("A", 1, "title1", "fullTitle1",
				1996, "image1", "crew1", 10.5, 10L);
		ImdbMovieDTO movie2 = new ImdbMovieDTO("B", 3, "title3", "fullTitle3",
				1996, "image3", "crew3", 12.5, 10L);
		ImdbTop250MoviesResponse imdbTop250MoviesResponse = Mockito.mock(ImdbTop250MoviesResponse.class);
		Mockito.when(imdbTop250MoviesResponse.getMovieById("A")).thenReturn(movie1);
		Mockito.when(imdbTop250MoviesResponse.getMovieById("B")).thenReturn(movie2);
		Mockito.when(this.imdbClient.get250Movies()).thenReturn(imdbTop250MoviesResponse);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getName()).thenReturn("gustavo");
		
		
		this.imdbService.setMovies();
		UserDTO userInfo = this.imdbService.getTwoMoviesResult("A", "B",
				"A", "id1", authentication);
		
		Assertions.assertTrue(userInfo.getWrongChoice() == 1);
		Assertions.assertTrue(userInfo.getPoint() == 0);
		Assertions.assertTrue(userInfo.getUserLost().booleanValue());
	}
	
	@Test
	public void getTwoMoviesResultChoiceCorrect() {
		Mockito.when(this.userActionRepository.countWrongChoice("id1")).thenReturn(1);
		Mockito.when(this.userActionRepository.save(Mockito.any(UserAction.class))).thenReturn(null);
		
		ImdbMovieDTO movie1 = new ImdbMovieDTO("A", 1, "title1", "fullTitle1",
				1996, "image1", "crew1", 12.5, 10L);
		ImdbMovieDTO movie2 = new ImdbMovieDTO("B", 3, "title3", "fullTitle3",
				1996, "image3", "crew3", 10.5, 10L);
		ImdbTop250MoviesResponse imdbTop250MoviesResponse = Mockito.mock(ImdbTop250MoviesResponse.class);
		Mockito.when(imdbTop250MoviesResponse.getMovieById("A")).thenReturn(movie1);
		Mockito.when(imdbTop250MoviesResponse.getMovieById("B")).thenReturn(movie2);
		Mockito.when(this.imdbClient.get250Movies()).thenReturn(imdbTop250MoviesResponse);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getName()).thenReturn("gustavo");
		
		
		this.imdbService.setMovies();
		UserDTO userInfo = this.imdbService.getTwoMoviesResult("A", "B",
				"A", "id1", authentication);
		
		Assertions.assertTrue(userInfo.getWrongChoice() == 0);
		Assertions.assertTrue(userInfo.getPoint() == 1);
		Assertions.assertFalse(userInfo.getUserLost().booleanValue());
	}
	
	@Test
	public void getTwoMoviesResultChoiceCorrectMovie2Selected() {
		Mockito.when(this.userActionRepository.countWrongChoice("id1")).thenReturn(null);
		Mockito.when(this.userActionRepository.save(Mockito.any(UserAction.class))).thenReturn(null);
		
		ImdbMovieDTO movie1 = new ImdbMovieDTO("A", 1, "title1", "fullTitle1",
				1996, "image1", "crew1", 10.5, 10L);
		ImdbMovieDTO movie2 = new ImdbMovieDTO("B", 3, "title3", "fullTitle3",
				1996, "image3", "crew3", 12.5, 10L);
		ImdbTop250MoviesResponse imdbTop250MoviesResponse = Mockito.mock(ImdbTop250MoviesResponse.class);
		Mockito.when(imdbTop250MoviesResponse.getMovieById("A")).thenReturn(movie1);
		Mockito.when(imdbTop250MoviesResponse.getMovieById("B")).thenReturn(movie2);
		Mockito.when(this.imdbClient.get250Movies()).thenReturn(imdbTop250MoviesResponse);
		
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getName()).thenReturn("gustavo");
		
		
		this.imdbService.setMovies();
		UserDTO userInfo = this.imdbService.getTwoMoviesResult("A", "B",
				"B", "id1", authentication);
		
		Assertions.assertTrue(userInfo.getWrongChoice() == 0);
		Assertions.assertTrue(userInfo.getPoint() == 1);
		Assertions.assertFalse(userInfo.getUserLost().booleanValue());
	}
	
}
