package com.br.MoviesBattle.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.br.MoviesBattle.resource.MatchResource;
import com.br.MoviesBattle.resource.MovieResource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private BasicAuthentication basicAuthentication;
    
    public SecurityConfig(BasicAuthentication basicAuthentication) {
    	this.basicAuthentication = basicAuthentication;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
			.authenticationEntryPoint(basicAuthentication)
			.and()
			.logout(logout -> logout
					.logoutUrl(MatchResource.MATCH_FINISH)
					.logoutSuccessUrl(MatchResource.MATCH_START)
			        .invalidateHttpSession(true)                                        
			        .deleteCookies()
			        )
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()
			.authorizeRequests().antMatchers("/h2-console/**").permitAll()
			.and()
			.authorizeRequests().antMatchers(MatchResource.MATCH_START).authenticated()
			.and()
			.authorizeRequests().antMatchers(MatchResource.MATCH_RANKING).authenticated()
			.and()
			.authorizeRequests().antMatchers(MovieResource.MOVIE_TWO_MOVIES).authenticated()
			.and()
			.authorizeRequests().antMatchers(MovieResource.MOVIE_TWO_MOVIES_RESULT).authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("teste").password(passwordEncoder().encode("123456")).authorities("ROLE_USER")
			.and()
			.withUser("gustavo").password(passwordEncoder().encode("123456")).authorities("ROLE_USER");
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
