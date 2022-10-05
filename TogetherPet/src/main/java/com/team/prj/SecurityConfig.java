package com.team.prj;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.team.prj.login.service.UService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public UService userService() {
		return new UService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CustomLoginSuccessHandler loginHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new WebAccessDenyHandler();
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> 
							requests
							.antMatchers("/", "/index", "/join/**", "/login", "/shop", "/member/**", "/assets/**", "/admin/**", "/fullcalendar/**","/js/**",
									"/joinMain", "/loginForm", "/sellerJoinForm", "/tutorJoinForm", "/userJoinForm").permitAll()
							.antMatchers("/manager/**").hasAuthority("ROLE_ADMIN")
							.anyRequest().authenticated())
				.formLogin().loginPage("/loginForm")
							.failureUrl("/loginForm")
							.usernameParameter("id")
							.successHandler(loginHandler())
							.loginProcessingUrl("/login")
							//.defaultSuccessUrl("/index")
							.and()
				.logout().logoutUrl("/logout")
					      .logoutSuccessUrl("/index")
						 .invalidateHttpSession(true)
						 .deleteCookies("JSESSIONID")
						 .and()
				.exceptionHandling()
					     .accessDeniedHandler(accessDeniedHandler())
					     .and()
				// .csrf().disable()
				.userDetailsService(userService());

		return http.build();
	}
	
	
	/* 희수 수정 (1005)
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/assets/**", "/admin/**", "/fullcalendar/**","/js/**");
	}
	 */
	
}
