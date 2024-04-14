package com.chris.ims.entity.security;

import com.chris.ims.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthFacade {
  
  private AuthFacade() {
    
  }
  
  public static User getCurrentUser() {
    var context = SecurityContextHolder.getContext();
    if (context.getAuthentication() != null && context.getAuthentication().getDetails() instanceof UserDetailsImpl userDetails) {
      return userDetails.getUser();
    }
    return null;
  }
}
