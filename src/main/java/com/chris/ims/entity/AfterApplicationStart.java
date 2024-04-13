package com.chris.ims.entity;

import com.chris.ims.entity.user.User;
import com.chris.ims.entity.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AfterApplicationStart {
  private static final String ADMIN_STRING = "ADMIN";

  private final UserFacade userFacade;


  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    // if admin user does not exist then create it and set default password
    if (!userFacade.existsByUsername(ADMIN_STRING)) {
      User admin = userFacade.newEntity();
      admin.setName(ADMIN_STRING);
      admin.setUsername(ADMIN_STRING);
      admin.setPassword("a12345");
      userFacade.save(admin);
    }



  }
}
