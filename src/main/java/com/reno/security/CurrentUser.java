package com.reno.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public final class CurrentUser {
 private CurrentUser() {}
 public static String subject(Authentication authentication){
  if(authentication==null||!(authentication.getPrincipal() instanceof Jwt jwt)) throw new IllegalStateException("Authenticated JWT is required");
  return jwt.getSubject();
 }
 public static String role(Authentication authentication){
  if(authentication==null) return null;
  return authentication.getAuthorities().stream().map(a->a.getAuthority()).filter(a->a.startsWith("ROLE_")).findFirst().orElse(null);
 }
}
