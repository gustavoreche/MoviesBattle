package com.br.MoviesBattle.model.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user_action")
public class UserAction {

	public UserAction() {
	}

	public UserAction(String idGame, String user, String idMovie1, String idMovie2, Integer point, Integer wrongChoice,
			Boolean userLost) {
		this.idGame = idGame;
		this.user = user;
		this.idMovie1 = idMovie1;
		this.idMovie2 = idMovie2;
		this.point = point;
		this.wrongChoice = wrongChoice;
		this.userLost = userLost;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String idGame;

	private String user;

	private String idMovie1;

	private String idMovie2;

	private Integer point;

	private Integer wrongChoice;

	private Boolean userLost;

	public Integer getId() {
		return id;
	}
	
	public String getIdGame() {
		return idGame;
	}

	public String getUser() {
		return user;
	}

	public String getIdMovie1() {
		return idMovie1;
	}

	public String getIdMovie2() {
		return idMovie2;
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
