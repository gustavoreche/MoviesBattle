package com.br.MoviesBattleAplicacao.model.login;

import org.springframework.http.HttpHeaders;

public class LoginDTO {

	private HttpHeaders authorizationSuccess;
	private String idGame;
	
	public LoginDTO() {
	}

	public LoginDTO(HttpHeaders authorizationSuccess, String idGame) {
		this.authorizationSuccess = authorizationSuccess;
		this.idGame = idGame;
	}

	public HttpHeaders getAuthorizationSuccess() {
		return authorizationSuccess;
	}

	public String getIdGame() {
		return idGame;
	}

}
