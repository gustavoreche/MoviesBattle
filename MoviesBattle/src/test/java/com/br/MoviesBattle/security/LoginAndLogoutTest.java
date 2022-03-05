package com.br.MoviesBattle.security;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginAndLogoutTest {
	
	@LocalServerPort
	private int port = 8080;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void loginOneTimeAndKeepLogin() throws Exception {
		ResponseEntity<String> responseOfRequest = executeStartRequestWithAutentication();
		Assertions.assertTrue(responseOfRequest.getStatusCode() == HttpStatus.OK);
		Assertions.assertTrue(responseOfRequest.getBody().equals("Start match"));
		
		List<String> cookieHeader = responseOfRequest.getHeaders().get("Set-Cookie");
		
		final HttpHeaders headersSecondRequest = new HttpHeaders();
		headersSecondRequest.addAll("Cookie", cookieHeader);
		ResponseEntity<String> responseOfSecondRequest = executeRequest(headersSecondRequest, "/start");
		Assertions.assertTrue(responseOfSecondRequest.getStatusCode() == HttpStatus.OK);
		Assertions.assertTrue(responseOfSecondRequest.getBody().equals("Start match"));
	}
	
	private ResponseEntity<String> executeStartRequestWithAutentication() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("gustavo", "123456");
		return executeRequest(headers, "/start");
	}
	
	private ResponseEntity<String> executeRequest(HttpHeaders headers, String path) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return this.restTemplate
				.exchange("http://localhost:" + port + "/match" + path,
						HttpMethod.GET,
						entity,
						String.class);
	}
	
	@Test
	public void loginAndLogout() throws Exception {
		ResponseEntity<String> responseOfRequest = executeStartRequestWithAutentication();
		Assertions.assertTrue(responseOfRequest.getStatusCode() == HttpStatus.OK);
		Assertions.assertTrue(responseOfRequest.getBody().equals("Start match"));
		
		final HttpHeaders headersFinishRequest = new HttpHeaders();
		executeRequest(headersFinishRequest, "/finish");

		final HttpHeaders headersSecondRequest = new HttpHeaders();
		ResponseEntity<String> responseOfSecondRequest = executeRequest(headersSecondRequest, "/start");
		Assertions.assertTrue(responseOfSecondRequest.getStatusCode() == HttpStatus.UNAUTHORIZED);
		
	}
	
}
