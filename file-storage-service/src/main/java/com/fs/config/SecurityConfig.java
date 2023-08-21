package com.fs.config;

import com.fs.security.TokenFilter;
import com.fs.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private TokenProvider tokenProvider;

  @Bean
  public TokenProvider tokenProvider(@Value("${application.security-secret}") String secret) {
    TokenProvider tokenProvider = new TokenProvider();
    tokenProvider.setSecretKey(secret);
    this.tokenProvider = tokenProvider;
    return tokenProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf()
        .disable()
        .addFilterBefore(new TokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/storage/object/**")
        .permitAll()
        .antMatchers("/api/storage/**")
        .authenticated()
        .antMatchers("/api/**")
        .permitAll()
        .and()
        .build();
  }
}