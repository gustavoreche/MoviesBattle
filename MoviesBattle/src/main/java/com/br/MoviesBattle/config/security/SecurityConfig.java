package com.br.MoviesBattle.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
					.logoutUrl("/match/finish")
					.logoutSuccessUrl("/match/start")
			        .invalidateHttpSession(true)                                        
			        .deleteCookies()
			        )
			.csrf().disable()
			.authorizeRequests().antMatchers("/match/start").authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("gustavo").password(passwordEncoder().encode("123456"))
		.authorities("ROLE_USER");
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
