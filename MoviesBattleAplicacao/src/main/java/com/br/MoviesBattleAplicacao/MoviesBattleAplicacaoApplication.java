package com.br.MoviesBattleAplicacao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpHeaders;

import com.br.MoviesBattleAplicacao.model.ImdbMovieDTO;
import com.br.MoviesBattleAplicacao.model.ImdbTwoMoviesResponse;
import com.br.MoviesBattleAplicacao.model.login.LoginDTO;
import com.br.MoviesBattleAplicacao.model.user.RankingDTO;
import com.br.MoviesBattleAplicacao.model.user.UserDTO;
import com.br.MoviesBattleAplicacao.service.MovieBattleService;

@SpringBootApplication
@EnableFeignClients
public class MoviesBattleAplicacaoApplication implements CommandLineRunner {
	
	private MovieBattleService movieBattleService;
	private Scanner inputUser;
	
	public MoviesBattleAplicacaoApplication(MovieBattleService movieBattleService) {
		this.movieBattleService = movieBattleService;
		this.inputUser = new Scanner(System.in);
	}

	public static void main(String[] args) {
		SpringApplication.run(MoviesBattleAplicacaoApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		boolean playGame = true;
		while(playGame) {
			startGame();
			System.out.print("Deseja jogar novamente? Digite SIM ou NAO: ");
			Thread.sleep(200);
			playGame = "SIM".equalsIgnoreCase(this.inputUser.nextLine());
		}
		System.err.println("VOCE ENCERROU O JOGO. MUITO OBRIGADO!!");
		Thread.sleep(200);
	}

	private void startGame() throws InterruptedException {
		welcomeGame();
		boolean authenticated = false;
		HttpHeaders authorizationSuccess = new HttpHeaders();
		LoginDTO loginInfo = new LoginDTO();
		while(!authenticated) {
			System.out.print("Digite o NOME do USUARIO: ");
			Thread.sleep(200);
			String user = this.inputUser.nextLine();
			isExitGame(user);
			System.out.print("Digite a SENHA do USUARIO: ");
			Thread.sleep(200);
			String password = this.inputUser.nextLine();
			isExitGame(password);
			loginInfo = this.movieBattleService.login(user, password);
			authorizationSuccess = loginInfo.getAuthorizationSuccess();
			if(Objects.nonNull(authorizationSuccess)) {
				authenticated = true;
			}
		}
		
		System.out.print("Deseja visualizar o RANKING dos melhores jogadores? Digite SIM ou NAO: ");
		Thread.sleep(200);
		String wantShowRanking = this.inputUser.nextLine();
		isExitGame(wantShowRanking);
		boolean showRanking = "SIM".equalsIgnoreCase(wantShowRanking);
		if(showRanking) {
			List<RankingDTO> ranking = this.movieBattleService.getRanking(authorizationSuccess);
			if(ranking.size() == 0) {
				System.out.println("----------------------------------------------------------------------------------");
				Thread.sleep(200);
				System.out.println("O ranking ainda esta vazio!");
				Thread.sleep(200);
				System.out.println("----------------------------------------------------------------------------------");
				Thread.sleep(200);
			}
			Collections.reverse(ranking);
			for(int rankingPosition = 0; rankingPosition < ranking.size(); rankingPosition++) {
				System.out.println("------------------------------------------------------------------------------------");
				Thread.sleep(200);
				System.out.println((rankingPosition + 1) + " - " + ranking.get(rankingPosition).getUser() 
						+ " | Pontuacao: " + ranking.get(rankingPosition).getPontuation());
				Thread.sleep(200);
				System.out.println("------------------------------------------------------------------------------------");
				Thread.sleep(200);
			}
		}
		
		UserDTO userInformation = new UserDTO(false);
		String idGame = "";
		while(!userInformation.getUserLost()) {
			ImdbTwoMoviesResponse movies = this.movieBattleService.getTwoMovies(authorizationSuccess, idGame);
			ImdbMovieDTO movie1 = movies.getMovie1();
			ImdbMovieDTO movie2 = movies.getMovie2();
			boolean chosenMovie = false;
			while(!chosenMovie) {
				showMovie("FILME 1: ", movie1);
				showMovie("FILME 2: ", movie2);
				System.out.print("- Digite 1 ou 2 para escolher o filme com melhor pontuacao: ");
				Thread.sleep(200);
				String inputUserMovieSelected = this.inputUser.nextLine();
				isExitGame(inputUserMovieSelected);
				ImdbMovieDTO movieSelected = new ImdbMovieDTO();
				if("1".equalsIgnoreCase(inputUserMovieSelected)) {
					movieSelected = movie1;
					chosenMovie = true;
				} else if("2".equalsIgnoreCase(inputUserMovieSelected)) {
					movieSelected = movie2;
					chosenMovie = true;
				} else {
					System.err.println("OPCAO INVALIDA!! Tente novamente");
					Thread.sleep(200);
					chosenMovie = false;
				}
				if(chosenMovie) {
					idGame = loginInfo.getIdGame();
					userInformation = this.movieBattleService.getTwoMoviesResult(authorizationSuccess, movies, movieSelected, idGame);
					showResult(userInformation);					
				}
			}
		}
		System.err.println("------------------------------------------------------------------------");
		Thread.sleep(200);
		System.err.println("VOCE PERDEU");
		Thread.sleep(200);
		System.err.println("------------------------------------------------------------------------");
		Thread.sleep(200);
		this.movieBattleService.logout(authorizationSuccess);
	}

	private void showResult(UserDTO userInformation) throws InterruptedException {
		System.out.println("------------------------------------------------------------------------------------------------------------------------");
		Thread.sleep(200);
		if(userInformation.getPoint() > 0) {
			System.out.println("PARABENS, voce acertou o filme de maior pontuacao!!!!");
			Thread.sleep(200);
		} else {
			System.out.println("ERROU!! Infelizmente voce errou o filme de maior pontuacao. Voce pode errar 3 vezes");
			Thread.sleep(200);			
		}
		System.out.println("------------------------------------------------------------------------------------------------------------------------");
		Thread.sleep(200);
	}

	private void showMovie(String movieText, ImdbMovieDTO movie) throws InterruptedException {
		System.out.println(movieText + movie.getFullTitle() + ". Para ver o poster do filme, copie a seguinte URL e cole no seu navegador: " + movie.getImage());
		Thread.sleep(200);
	}

	private void welcomeGame() throws InterruptedException {
		System.err.println("BEM VINDO AO MOVIES BATTLE");
		Thread.sleep(200);
		System.out.println("* COMO FUNCIONA: Sera exibido 2 filmes. Voce devera escolher o que possui maior pontuacao.");
		Thread.sleep(200);
		System.out.println("* Para sair do jogo a qualquer momento, digite SAIR.");
		Thread.sleep(200);
		System.out.println("-------------------------------------------------------------------------------------------------------");
		Thread.sleep(200);
		System.out.println("-- Primeiro eh necessario realizar o login --");
		Thread.sleep(200);
	}
	
	private void isExitGame(String inputUser) throws InterruptedException {
		if("SAIR".equalsIgnoreCase(inputUser)) {
			System.err.println("VOCE ENCERROU O JOGO. MUITO OBRIGADO!!");
			Thread.sleep(200);
			System.exit(0);
		}
	}

}
