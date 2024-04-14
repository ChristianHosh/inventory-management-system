package com.chris.ims.entity.security;

import com.chris.ims.entity.exception.BxException;
import com.chris.ims.entity.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.chris.ims.entity.security.AuthController.JwtDto;
import static com.chris.ims.entity.security.AuthController.LoginRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserFacade userFacade;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  public JwtDto login(LoginRequest request) {
    if (userFacade.existsByUsername(request.username())) {
      var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
      var authentication = authenticationManager.authenticate(token);

      return new JwtDto(jwtUtils.generateJwtToken(authentication));
    } else {
      throw BxException.unauthorized("bad credentials");
    }
  }
}
