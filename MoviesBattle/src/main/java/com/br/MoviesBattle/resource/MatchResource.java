package com.br.MoviesBattle.resource;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.MoviesBattle.model.user.RankingDTO;
import com.br.MoviesBattle.service.MatchService;

@RestController
public class MatchResource {
	
	public static final String MATCH_START = "/match/start";
	public static final String MATCH_FINISH = "/match/finish";
	public static final String MATCH_RANKING = "/match/ranking";
	
	private MatchService matchService;
	
	public MatchResource(MatchService matchService) {
		this.matchService = matchService;
	}

	@GetMapping(MATCH_START)
	public String start() {
		return UUID.randomUUID().toString();
	}
	
	@GetMapping(MATCH_FINISH)
	public void finish() {
	}
	
	@GetMapping(MATCH_RANKING)
	public List<RankingDTO> ranking() {
		return this.matchService.getRanking();
	}

}
