package com.br.MoviesBattleAplicacao;

import java.util.Objects;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpHeaders;

import com.br.MoviesBattleAplicacao.service.MovieBattleService;

@SpringBootApplication
@EnableFeignClients
public class MoviesBattleAplicacaoApplication implements CommandLineRunner {
	
	private MovieBattleService movieBattleService;
	
	public MoviesBattleAplicacaoApplication(MovieBattleService movieBattleService) {
		this.movieBattleService = movieBattleService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MoviesBattleAplicacaoApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.err.println("BEM VINDO AO MOVIES BATTLE");
		Thread.sleep(200);
		System.out.println("COMO FUNCIONA: Sera exibido 2 filmes. Voce devera escolher o que possui maior pontuacao.");
		System.out.println("Para sair do jogo a qualquer momento, digite SAIR.");
		Thread.sleep(200);
		System.out.println("-------------------------------------------------------------------------------------------------------");
		Thread.sleep(200);
		System.out.println("Primeiro eh necessario realizar o login");
		Thread.sleep(200);
		boolean authenticated = false;
		HttpHeaders authorizationSuccess = null;
		while(!authenticated) {
			Scanner inputUser = new Scanner(System.in);
			System.out.print("Digite o NOME do USUARIO: ");
			String user = inputUser.nextLine();
			isExitGame(user);
			Thread.sleep(200);
			System.out.print("Digite a SENHA do USUARIO: ");
			String password = inputUser.nextLine();
			isExitGame(password);
			Thread.sleep(200);
			authorizationSuccess = this.movieBattleService.login(user, password);
			if(Objects.nonNull(authorizationSuccess)) {
				authenticated = true;
			}
		}
		this.movieBattleService.get250Movies(authorizationSuccess);
	}
	
	private void isExitGame(String inputUser) throws InterruptedException {
		if("SAIR".equalsIgnoreCase(inputUser)) {
			System.err.println("VOCE ENCERROU O JOGO. MUITO OBRIGADO!!");
			Thread.sleep(200);
			System.exit(0);
		}
	}

}
