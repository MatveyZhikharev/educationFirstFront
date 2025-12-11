package com.educationalplatform.utils;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class IdAuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;

  public IdAuthenticationToken(Object principal) {
    super(null);
    this.principal = principal;
    setAuthenticated(false);
  }

  public IdAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null; // пароля нет
  }

  @Override
  public Object getPrincipal() {
    return principal; // возвращаем объект User
  }
}
