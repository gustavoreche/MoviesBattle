package com.br.MoviesBattle.service;

import org.springframework.stereotype.Service;

import com.br.MoviesBattle.client.ImdbClient;
import com.br.MoviesBattle.model.imdb.ImdbTop250MoviesResponse;

@Service
public class ImdbService {
	
	private ImdbClient imdbClient;

	public ImdbService(ImdbClient imdbClient) {
		this.imdbClient = imdbClient;
	}
	
	public ImdbTop250MoviesResponse getTop250Movies() {
		return this.imdbClient.get250Films();
	}
	
}
