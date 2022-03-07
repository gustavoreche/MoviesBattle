package com.br.MoviesBattle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.br.MoviesBattle.model.database.Ranking;
import com.br.MoviesBattle.model.database.UserAction;

public interface UserActionRepository extends CrudRepository<UserAction, Integer> {
	
	List<UserAction> findByIdGame(String idGame);
	
	@Query(value = "SELECT SUM(wrong_choice) FROM tb_user_action WHERE id_game = :idGame", 
			nativeQuery = true)
	Integer countWrongChoice(String idGame);
	
	@Query(value = "SELECT user, COUNT(id) AS quiz, SUM(point) AS correct FROM tb_user_action GROUP BY id_game;", 
			nativeQuery = true)
	List<Ranking> getRanking();

}
