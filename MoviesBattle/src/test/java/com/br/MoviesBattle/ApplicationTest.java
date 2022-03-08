package com.br.MoviesBattle;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.br.MoviesBattle.model.database.UserAction;
import com.br.MoviesBattle.model.imdb.ImdbMovieDTO;
import com.br.MoviesBattle.model.imdb.ImdbTwoMoviesResponse;
import com.br.MoviesBattle.model.user.UserDTO;
import com.br.MoviesBattle.resource.MovieResource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void loginOneTimeAndKeepLogin() throws Exception {
		ResponseEntity<String> responseOfRequest = executeStartRequestWithAutentication();
		Assertions.assertEquals(HttpStatus.OK, responseOfRequest.getStatusCode());
		
		List<String> cookieHeader = responseOfRequest.getHeaders().get("Set-Cookie");
		
		final HttpHeaders headersSecondRequest = new HttpHeaders();
		headersSecondRequest.addAll("Cookie", cookieHeader);
		ResponseEntity<String> responseOfSecondRequest = executeMatchRequest(headersSecondRequest, "/start");
		Assertions.assertEquals(HttpStatus.OK, responseOfSecondRequest.getStatusCode());
	}
	
	private ResponseEntity<String> executeStartRequestWithAutentication() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("gustavo", "123456");
		return executeMatchRequest(headers, "/start");
	}
	
	private ResponseEntity<String> executeMatchRequest(HttpHeaders headers, String path) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return this.restTemplate
				.exchange("http://localhost:" + port + "/match" + path,
						HttpMethod.POST,
						entity,
						String.class);
	}
	
	@Test
	public void loginAndLogout() throws Exception {
		ResponseEntity<String> responseOfRequest = executeStartRequestWithAutentication();
		Assertions.assertEquals(HttpStatus.OK, responseOfRequest.getStatusCode());
		
		final HttpHeaders headersFinishRequest = new HttpHeaders();
		executeMatchRequest(headersFinishRequest, "/finish");

		final HttpHeaders headersSecondRequest = new HttpHeaders();
		ResponseEntity<String> responseOfSecondRequest = executeMatchRequest(headersSecondRequest, "/start");
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseOfSecondRequest.getStatusCode());
	}
	
	@Test
	public void getTwoMoviesWithoutAuthorization() throws Exception {
		final HttpHeaders headers = new HttpHeaders();
		ResponseEntity<ImdbTwoMoviesResponse> responseOfRequest = executeMovieRequest(headers, "");
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseOfRequest.getStatusCode());
	}
	
	private ResponseEntity<ImdbTwoMoviesResponse> executeMovieRequest(HttpHeaders headers, String idGame) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return this.restTemplate
				.exchange("http://localhost:" + port 
						+ MovieResource.MOVIE_TWO_MOVIES + "?idGame=" + idGame,
						HttpMethod.GET,
						entity,
						ImdbTwoMoviesResponse.class);
	}
	
	@Test
	public void getNormalFlow() throws Exception {
		ResponseEntity<String> loginRequest = executeStartRequestWithAutentication();
		Assertions.assertEquals(HttpStatus.OK, loginRequest.getStatusCode());
		
		List<String> cookieHeader = loginRequest.getHeaders().get("Set-Cookie");
		
		UserDTO userInfo = new UserDTO(new UserAction());
		boolean continueGame = true;
		while(continueGame) {
			final HttpHeaders headersTwoMoviesRequest = new HttpHeaders();
			headersTwoMoviesRequest.addAll("Cookie", cookieHeader);
			ResponseEntity<ImdbTwoMoviesResponse> responseTwoMoviesRequest = executeMovieRequest(headersTwoMoviesRequest, loginRequest.getBody());
			Assertions.assertEquals(HttpStatus.OK, responseTwoMoviesRequest.getStatusCode());
			
			ImdbMovieDTO movie1 = responseTwoMoviesRequest.getBody().getMovie1();
			ImdbMovieDTO movie2 = responseTwoMoviesRequest.getBody().getMovie2();
			
			ImdbMovieDTO wrongChoice = movie1.getPontuation() < movie2.getPontuation()
					? movie1
					: movie2;
			
			final HttpHeaders headersTwoMoviesResultRequest = new HttpHeaders();
			headersTwoMoviesResultRequest.addAll("Cookie", cookieHeader);
			ResponseEntity<UserDTO> responseTwoMoviesResultRequest = executeMovieResultRequest(headersTwoMoviesResultRequest, loginRequest.getBody(),
					responseTwoMoviesRequest.getBody().getMovie1().getId(), 
					responseTwoMoviesRequest.getBody().getMovie2().getId(),
					wrongChoice.getId());
			Assertions.assertEquals(HttpStatus.OK, responseTwoMoviesResultRequest.getStatusCode());
			continueGame = !responseTwoMoviesResultRequest.getBody().getUserLost().booleanValue();
			userInfo = responseTwoMoviesResultRequest.getBody();
		}
		
		Assertions.assertTrue(userInfo.getUserLost());
	}
	
	private ResponseEntity<UserDTO> executeMovieResultRequest(HttpHeaders headers, String idGame,
			String idMovie1, String idMovie2, String idMovieSelected) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return this.restTemplate
				.exchange("http://localhost:" + port 
						+ MovieResource.MOVIE_TWO_MOVIES_RESULT 
						+ "?idMovie1=" + idMovie1
						+ "&idMovie2=" + idMovie2
						+ "&idMovieSelect=" + idMovieSelected
						+ "&idGame=" + idGame,
						HttpMethod.GET,
						entity,
						UserDTO.class);
	}
	
}
