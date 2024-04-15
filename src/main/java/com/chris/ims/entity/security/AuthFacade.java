package com.chris.ims.entity.security;

import com.chris.ims.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;

public final class AuthFacade {

  private AuthFacade() {

  }

  public static User getCurrentUser() {
    UserDetailsImpl details = getDetails();
    return details == null ? null : details.getUser();
  }

  public static UserDetailsImpl getDetails() {
    var context = SecurityContextHolder.getContext();
    if (context.getAuthentication() != null && context.getAuthentication().getDetails() instanceof UserDetailsImpl userDetails) {
      return userDetails;
    }
    return null;
  }

  public static Locale getCurrentLocale() {
    User user = getCurrentUser();
    return user == null ? Locale.ENGLISH : Locale.of(user.getString(User.F_DEFAULT_LOCALE));
  }
}
