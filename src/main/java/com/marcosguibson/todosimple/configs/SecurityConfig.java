package com.marcosguibson.todosimple.configs;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  // * LISTA DE ROTAS PUBLICAS
  private static final String[] PUBLIC_MATHERS = {
      "/"
  };

  // * LISTA DE ROTAS PUBLICAS PARA POST
  private static final String[] PUBLIC_MATHERS_POST = {
      "/user",
      "/login"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // *Desativar request cros origin de vairas requisicoes
    http.cors().and().csrf().disable();

    http.authorizeHttpRequests()
        .antMatchers(HttpMethod.POST, PUBLIC_MATHERS_POST).permitAll()
        .antMatchers(PUBLIC_MATHERS).permitAll()
        .anyRequest().authenticated();

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
    configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // * Serve para criptografar 
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
