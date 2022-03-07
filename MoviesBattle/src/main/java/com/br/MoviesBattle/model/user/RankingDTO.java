package com.br.MoviesBattle.model.user;

public class RankingDTO implements Comparable<RankingDTO> {

	private String user;
	private Double pontuation;

	public RankingDTO(String user, Double pontuation) {
		this.user = user;
		this.pontuation = pontuation;
	}

	public String getUser() {
		return user;
	}

	public Double getPontuation() {
		return pontuation;
	}
	
	@Override
	public int compareTo(RankingDTO ranking) {
		return ranking.getPontuation().compareTo(this.pontuation);
	}

}
