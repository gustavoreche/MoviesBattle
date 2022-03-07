package com.br.MoviesBattle.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.br.MoviesBattle.model.imdb.ImdbTop250MoviesResponse;

@FeignClient(value = "imdb", url = "${host.imdb}")
public interface ImdbClient {
	
	@GetMapping("en/API/Top250Movies/k_4g22eka3")
	ImdbTop250MoviesResponse get250Movies();

}
