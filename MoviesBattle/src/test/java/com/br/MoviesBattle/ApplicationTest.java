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
		ResponseEntity<String> responseOfRequest = executeMovieRequest(headers);
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseOfRequest.getStatusCode());
	}
	
	private ResponseEntity<String> executeMovieRequest(HttpHeaders headers) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return this.restTemplate
				.exchange("http://localhost:" + port + MovieResource.MOVIE_TWO_MOVIES,
						HttpMethod.GET,
						entity,
						String.class);
	}
	
	@Test
	public void getTwoMoviesWithAuthorization() throws Exception {
		ResponseEntity<String> responseOfRequest = executeStartRequestWithAutentication();
		Assertions.assertEquals(HttpStatus.OK, responseOfRequest.getStatusCode());
		
		List<String> cookieHeader = responseOfRequest.getHeaders().get("Set-Cookie");
		
		final HttpHeaders headersSecondRequest = new HttpHeaders();
		headersSecondRequest.addAll("Cookie", cookieHeader);
		ResponseEntity<String> responseSecondOfRequest = executeMovieRequest(headersSecondRequest);
		Assertions.assertEquals(HttpStatus.OK, responseSecondOfRequest.getStatusCode());
	}
	
}
