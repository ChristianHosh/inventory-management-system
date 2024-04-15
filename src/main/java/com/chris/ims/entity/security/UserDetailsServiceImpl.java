package com.chris.ims.entity.security;

import com.chris.ims.entity.exception.CxException;
import com.chris.ims.entity.user.User;
import com.chris.ims.entity.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class UserDetailsServiceImpl implements UserDetailsService {

  private final UserFacade userFacade;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      User user = userFacade.findByUsername(username);

      List<GrantedAuthority> authorities = new ArrayList<>();
      if (user.isAdmin()) {
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
      } else {
        authorities.add(new SimpleGrantedAuthority("USER"));
      }
      return UserDetailsImpl.from(user, authorities);
    } catch (CxException e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }
}
