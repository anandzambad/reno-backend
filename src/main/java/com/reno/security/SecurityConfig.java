package com.reno.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
 @Value("${reno.security.allowed-origins:http://localhost:3000}") private String allowedOrigins;
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http.csrf(csrf->csrf.disable()).cors(cors->cors.configurationSource(corsConfigurationSource())).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a.requestMatchers("/actuator/health","/actuator/info","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll().anyRequest().authenticated())
   .oauth2ResourceServer(o->o.jwt(j->j.jwtAuthenticationConverter(jwtAuthenticationConverter())));
  return http.build();
 }
 @Bean Converter<Jwt,? extends AbstractAuthenticationToken> jwtAuthenticationConverter(){
  JwtGrantedAuthoritiesConverter scopes=new JwtGrantedAuthoritiesConverter();
  return jwt->{Set<String> roles=new HashSet<>(); Object claim=jwt.getClaims().get("roles"); if(claim instanceof Collection<?> c)c.forEach(v->roles.add("ROLE_"+String.valueOf(v).replaceFirst("^ROLE_",""))); else if(claim instanceof String s)Arrays.stream(s.split("[, ]+")).filter(v->!v.isBlank()).forEach(v->roles.add("ROLE_"+v.replaceFirst("^ROLE_",""))); var authorities=new ArrayList<>(scopes.convert(jwt)); authorities.addAll(roles.stream().map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).toList()); return new JwtAuthenticationToken(jwt,authorities,jwt.getSubject());};
 }
 @Bean CorsConfigurationSource corsConfigurationSource(){
  CorsConfiguration c=new CorsConfiguration(); c.setAllowedOrigins(Arrays.stream(allowedOrigins.split(",")).map(String::trim).filter(s->!s.isBlank()).toList()); c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS")); c.setAllowedHeaders(List.of("Authorization","Content-Type","Idempotency-Key","X-Request-Id")); c.setExposedHeaders(List.of("X-Request-Id")); c.setAllowCredentials(true); c.setMaxAge(3600L);
  UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",c); return source;
 }
}
