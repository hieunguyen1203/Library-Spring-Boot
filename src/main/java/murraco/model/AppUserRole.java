package murraco.model;

import org.springframework.security.core.GrantedAuthority;

public enum AppUserRole implements GrantedAuthority {
  ROLE_LIBRARIAN, ROLE_BORROWER;

  public String getAuthority() {
    return name();
  }

}
