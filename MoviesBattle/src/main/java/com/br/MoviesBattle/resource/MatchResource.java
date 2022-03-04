package com.br.MoviesBattle.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
public class MatchResource {
	
	@GetMapping("/start")
	public String start() {
		return "Start match";
	}

}
