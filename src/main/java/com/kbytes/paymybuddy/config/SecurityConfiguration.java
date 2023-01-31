package com.kbytes.paymybuddy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

	@Autowired
	PasswordEncoder passwordEncoder;

	private final UserDetailsService userDetailsService;

	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.userDetailsService(userDetailsService).authorizeRequests().antMatchers("/admin/**").hasAnyRole("ADMIN")
				.antMatchers("/users").hasAnyRole("ADMIN")
				.antMatchers("/register/**", "/css/**", "/images/**", "/webjars/**").permitAll().anyRequest()
				.authenticated().and().formLogin().loginPage("/login").permitAll().successForwardUrl("/index").and()
				.logout().permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login");
		return http.build();
	}

	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception {
	 * http.authorizeRequests().anyRequest().authenticated().and().formLogin().
	 * loginPage("/login").permitAll()
	 * .successForwardUrl("/index").and().logout().permitAll()
	 * .logoutRequestMatcher(new
	 * AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login"); return
	 * http.build(); }
	 */

	/*
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder auth,
	 * PasswordEncoder passwordEncoder) throws Exception {
	 * auth.inMemoryAuthentication().withUser("user").password(passwordEncoder.
	 * encode("password")).roles("USER").and()
	 * .withUser("admin").password(passwordEncoder.encode("admin")).roles("ADMIN");
	 * }
	 */
}