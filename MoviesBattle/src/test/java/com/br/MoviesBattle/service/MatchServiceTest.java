package com.br.MoviesBattle.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import com.br.MoviesBattle.model.database.RankingProjection;
import com.br.MoviesBattle.model.user.RankingDTO;
import com.br.MoviesBattle.repository.UserActionRepository;

public class MatchServiceTest {
	
	@Mock
	private UserActionRepository userActionRepository;
	
	@InjectMocks
	private MatchService matchService = new MatchService(this.userActionRepository);
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void getRanking() {
		ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
		
		Map<String, Object> infoUser1 = Map.of("user", "gustavo", "quiz", 10, "correct", 7);
		Map<String, Object> infoUser2 = Map.of("user", "teste", "quiz", 10, "correct", 4);
		Map<String, Object> infoUser3 = Map.of("user", "testeNumero1", "quiz", 10, "correct", 9);
		
		RankingProjection user1 = factory.createProjection(RankingProjection.class, infoUser1);
		RankingProjection user2 = factory.createProjection(RankingProjection.class, infoUser2);
		RankingProjection user3 = factory.createProjection(RankingProjection.class, infoUser3);
		
		List<RankingProjection> rankingProjection = List.of(user1, user2, user3);
		
		Mockito.when(this.userActionRepository.getRanking()).thenReturn(rankingProjection);
		List<RankingDTO> ranking = this.matchService.getRanking();
		
		Collections.reverse(ranking);
		RankingDTO bestUserOfRanking = ranking.stream().findFirst().get();
		
		Assertions.assertTrue(ranking.size() == 3);
		Assertions.assertTrue(bestUserOfRanking.getUser().equalsIgnoreCase("testeNumero1"));
		Assertions.assertTrue(bestUserOfRanking.getPontuation() == 90);
	}
	
	@Test
	public void getRankingWithoutData() {
		List<RankingProjection> rankingProjection = new ArrayList<RankingProjection>();
		Mockito.when(this.userActionRepository.getRanking()).thenReturn(rankingProjection);
		
		List<RankingDTO> ranking = this.matchService.getRanking();

		Assertions.assertTrue(ranking.size() == 0);
	}

}
