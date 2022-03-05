package com.br.MoviesBattleAplicacao.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "movieBattle", url = "http://localhost:8080/")
public interface MovieBattleClient {
	
	@GetMapping("match/start")
	ResponseEntity<String> login(@RequestHeader("Authorization") String header);
	
	@GetMapping("movie/top-movies")
	ResponseEntity<String> get250Movies(@RequestHeader("Authorization") HttpHeaders header);

}
