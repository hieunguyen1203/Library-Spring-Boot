package library.hieund.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
  ROLE_LIBRARIAN, ROLE_BORROWER;

  public String getAuthority() {
    return name();
  }

}
