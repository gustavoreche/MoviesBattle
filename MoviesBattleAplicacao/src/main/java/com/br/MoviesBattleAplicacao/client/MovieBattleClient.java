package com.br.MoviesBattleAplicacao.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.br.MoviesBattleAplicacao.model.ImdbTwoMoviesResponse;
import com.br.MoviesBattleAplicacao.model.user.RankingDTO;
import com.br.MoviesBattleAplicacao.model.user.UserDTO;

@FeignClient(value = "movieBattle", url = "http://localhost:8080/")
public interface MovieBattleClient {
	
	@GetMapping("match/start")
	ResponseEntity<String> login(@RequestHeader("Authorization") String header);
	
	@GetMapping("movie/two-movies")
	ResponseEntity<ImdbTwoMoviesResponse> getTwoMovies(@RequestHeader("Authorization") HttpHeaders header);
	
	@GetMapping("movie/two-movies/result")
	ResponseEntity<UserDTO> getTwoMoviesResult(@RequestHeader("Authorization") HttpHeaders header, 
			@RequestParam("idMovie1") String idMovie1,
			@RequestParam("idMovie2") String idMovie2,
			@RequestParam("idMovieSelect") String idMovieSelected,
			@RequestParam("idGame") String idGame);

	@GetMapping("match/finish")
	void logout(@RequestHeader("Authorization") HttpHeaders header);

	@GetMapping("match/ranking")
	List<RankingDTO> getRanking(@RequestHeader("Authorization") HttpHeaders header);

}
