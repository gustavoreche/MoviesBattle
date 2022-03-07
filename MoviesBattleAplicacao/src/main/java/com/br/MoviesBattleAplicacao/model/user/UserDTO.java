package com.br.MoviesBattleAplicacao.model.user;

public class UserDTO {
	
	private Integer point;
	private Integer wrongChoice;
	private Boolean userLost;

	public UserDTO() {
	}
	
	public UserDTO(Boolean userLost) {
		this.userLost = Boolean.FALSE;
	}
	
	public Integer getPoint() {
		return point;
	}
	
	public Integer getWrongChoice() {
		return wrongChoice;
	}
	
	public Boolean getUserLost() {
		return userLost;
	}

}
