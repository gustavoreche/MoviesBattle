package com.br.MoviesBattleAplicacao.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.br.MoviesBattleAplicacao.client.MovieBattleClient;
import com.br.MoviesBattleAplicacao.model.ImdbMovieDTO;
import com.br.MoviesBattleAplicacao.model.ImdbTwoMoviesResponse;
import com.br.MoviesBattleAplicacao.model.login.LoginDTO;
import com.br.MoviesBattleAplicacao.model.user.RankingDTO;
import com.br.MoviesBattleAplicacao.model.user.UserDTO;

import feign.FeignException;

@Service
public class MovieBattleService {
	
	private MovieBattleClient movieBattleClient;

	public MovieBattleService(MovieBattleClient movieBattleClient) {
		this.movieBattleClient = movieBattleClient;
	}
	
	public LoginDTO login(String user, String password) throws InterruptedException {
		try {
			byte[] basicAuth = Base64Utils.encode((user + ":" + password).getBytes());
	        String authHeader = "Basic " + new String(basicAuth);
			ResponseEntity<String> login = this.movieBattleClient.login(authHeader);	
			
			List<String> cookieHeader = login.getHeaders().get("Set-Cookie");
			final HttpHeaders authorizationSuccess = new HttpHeaders();
			authorizationSuccess.addAll("Cookie", cookieHeader);
			System.err.println("LOGADO com SUCESSO");
			Thread.sleep(200);
			return new LoginDTO(authorizationSuccess, login.getBody());
		} catch (FeignException clientError) {
			if (clientError.status() == HttpStatus.UNAUTHORIZED.value()) {
				System.err.println("ERRO!!!! Usuario OU Senha incorretos!!");
				Thread.sleep(200);
				System.err.println("----------------------------------------------");
				Thread.sleep(200);
			}
			return new LoginDTO();
		}
	}
	
	public ImdbTwoMoviesResponse getTwoMovies(HttpHeaders authorizationSuccess) throws InterruptedException {
		return this.movieBattleClient.getTwoMovies(authorizationSuccess).getBody();	
	}

	public UserDTO getTwoMoviesResult(HttpHeaders authorizationSuccess, 
			ImdbTwoMoviesResponse movies,
			ImdbMovieDTO movieSelected,
			String idGame) {
		return this.movieBattleClient.getTwoMoviesResult(authorizationSuccess, 
				movies.getMovie1().getId(),
				movies.getMovie2().getId(),
				movieSelected.getId(),
				idGame)
			.getBody();
	}
	
	public void logout(HttpHeaders authorizationSuccess) {
		this.movieBattleClient.logout(authorizationSuccess);
	}

	public List<RankingDTO> getRanking(HttpHeaders authorizationSuccess) {
		return this.movieBattleClient.getRanking(authorizationSuccess);
	}

}
