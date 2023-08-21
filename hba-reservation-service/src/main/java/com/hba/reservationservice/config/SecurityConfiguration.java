package com.hba.reservationservice.config;

import com.hba.hbacore.security.ServletTokenFilter;
import com.hba.hbacore.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public TokenProvider tokenProvider() {
    return TokenProvider.unsignedTokenProvider();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, TokenProvider tokenProvider)
      throws Exception {
    return http.csrf()
        .disable()
        .addFilterBefore(
            new ServletTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests()
        .antMatchers(HttpMethod.OPTIONS, "/api/**")
        .permitAll()
        .and()
        .build();
  }
}