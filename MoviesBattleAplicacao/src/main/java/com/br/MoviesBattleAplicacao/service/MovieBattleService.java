package com.br.MoviesBattleAplicacao.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.br.MoviesBattleAplicacao.client.MovieBattleClient;

import feign.FeignException;

@Service
public class MovieBattleService {
	
	private MovieBattleClient movieBattleClient;

	public MovieBattleService(MovieBattleClient movieBattleClient) {
		this.movieBattleClient = movieBattleClient;
	}
	
	public HttpHeaders login(String user, String password) throws InterruptedException {
		try {
			byte[] basicAuth = Base64Utils.encode((user + ":" + password).getBytes());
	        String authHeader = "Basic " + new String(basicAuth);
			ResponseEntity<String> login = this.movieBattleClient.login(authHeader);	
			
			List<String> cookieHeader = login.getHeaders().get("Set-Cookie");
			final HttpHeaders authorizationSuccess = new HttpHeaders();
			authorizationSuccess.addAll("Cookie", cookieHeader);
			System.err.println("LOGADO com SUCESSO");
			Thread.sleep(200);
			return authorizationSuccess;
		} catch (FeignException clientError) {
			if (clientError.status() == HttpStatus.UNAUTHORIZED.value()) {
				System.err.println("ERRO!!!! Usuario OU Senha incorretos!!");
				Thread.sleep(200);
				System.err.println("----------------------------------------------");
				Thread.sleep(200);
			}
			return null;
		}
	}
	
	public HttpHeaders get250Movies(HttpHeaders authorizationSuccess) throws InterruptedException {
		ResponseEntity<String> movies = this.movieBattleClient.get250Movies(authorizationSuccess);	
		return authorizationSuccess;
	}

}
