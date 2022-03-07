package com.br.MoviesBattle.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.br.MoviesBattle.model.database.Ranking;
import com.br.MoviesBattle.model.user.RankingDTO;
import com.br.MoviesBattle.repository.UserActionRepository;

@Service
public class MatchService {
	
	private UserActionRepository userActionRepository;

	public MatchService(UserActionRepository userActionRepository) {
		this.userActionRepository = userActionRepository;
	}
	
	public List<RankingDTO> getRanking() {
		return this.userActionRepository.getRanking()
			.stream()
			.map(ranking -> new RankingDTO(ranking.getUser(), getPontuation(ranking)))
			.collect(Collectors.toList());
	}
	
	private Double getPontuation(Ranking ranking) {
		return (Double.valueOf(ranking.getCorrect()) * 100) / ranking.getQuiz();
	}

}
