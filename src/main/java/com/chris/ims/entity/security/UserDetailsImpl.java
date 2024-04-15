package com.chris.ims.entity.security;

import com.chris.ims.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private final User user;

  private final Long id;

  private final String username;

  @JsonIgnore
  private final String password;

  @Getter
  private final List<? extends GrantedAuthority> authorities;
  
  public static UserDetailsImpl from(User user, List<? extends GrantedAuthority> authorities) {
    return new UserDetailsImpl(
        user,
        user.getId(),
        user.getField(User.F_USERNAME),
        user.getField(User.F_PASSWORD),
        authorities
    );
  }
  
  public Long getId() {
    return user.getId();
  }

  @Override
  public String getUsername() {
    return user.getString(User.F_USERNAME);
  }
  
  @Override
  public String getPassword() {
    return user.getString(User.F_USERNAME);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
