package com.reno.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public final class CurrentUser {
 private CurrentUser() {}
 private static Jwt jwt(Authentication authentication){
  if(authentication==null||!(authentication.getPrincipal() instanceof Jwt token)) throw new IllegalStateException("Authenticated JWT is required");
  return token;
 }
 public static String subject(Authentication authentication){ return jwt(authentication).getSubject(); }
 public static Long customerId(Authentication authentication){
  Object value=jwt(authentication).getClaims().get("customer_id");
  if(value instanceof Number n)return n.longValue();
  if(value instanceof String s)try{return Long.parseLong(s);}catch(NumberFormatException ignored){}
  throw new IllegalStateException("JWT is missing numeric customer_id claim");
 }
 public static String role(Authentication authentication){ return authentication.getAuthorities().stream().map(a->a.getAuthority()).filter(a->a.startsWith("ROLE_")).findFirst().orElse(null); }
}
