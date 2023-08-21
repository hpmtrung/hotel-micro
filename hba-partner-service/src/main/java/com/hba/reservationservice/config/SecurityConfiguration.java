package com.hba.reservationservice.config;

import com.hba.hbacore.security.ServletTokenFilter;
import com.hba.hbacore.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      PasswordEncoder encoder, UserDetailsService userDetailsService) {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    return daoAuthenticationProvider;
  }

  @Bean
  public ProviderManager providerManager(DaoAuthenticationProvider provider) {
    return new ProviderManager(provider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

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
        .antMatchers("/api/authenticate")
        .permitAll()
        .antMatchers("/api/account/**")
        .authenticated()
        .and()
        .build();
  }
}