package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//https://www.baeldung.com/spring-security-openid-connect
@Configuration
//@EnableWebSecurity
public class SecurityConfig {

	/*
	 * implementing OAUTH 2
	 */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
    	//FOR GOOGLE AND GITHUB AUTH
    	http
    	.formLogin()
    	.and()
    	.csrf().disable()
    	.authorizeHttpRequests()
    	.anyRequest().authenticated()
    	.and()
    	.oauth2Login();
    	
//    	 http
//    	 .csrf().disable()
//         .authorizeRequests(authorizeRequests -> authorizeRequests
//           .requestMatchers("/home").permitAll()
//           .anyRequest().authenticated())
//         .oauth2Login(oauthLogin -> oauthLogin.permitAll());
       
    	
    	return http.build();
	}
	
	
	
	/*
	 * IMPLEMENTING JWT authentication
	 */
	
	

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(csrf -> csrf.disable())
//        		.cors(cors -> cors.disable())
//                .authorizeHttpRequests(auth -> auth.
//		                requestMatchers("/courses").authenticated().
//		                requestMatchers("/").permitAll().
//		                requestMatchers("/auth/login").permitAll()
//		                .anyRequest().authenticated())
//                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        
//        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
}
