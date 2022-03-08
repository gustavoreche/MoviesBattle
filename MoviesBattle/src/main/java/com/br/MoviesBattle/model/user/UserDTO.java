package com.br.MoviesBattle.model.user;

import com.br.MoviesBattle.model.database.UserAction;

public class UserDTO {
	
	private Integer point;
	private Integer wrongChoice;
	private Boolean userLost;
	
	public UserDTO() {
	}
	
	public UserDTO(UserAction userAction) {
		this.point = userAction.getPoint();
		this.wrongChoice = userAction.getWrongChoice();
		this.userLost = userAction.getUserLost();
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
