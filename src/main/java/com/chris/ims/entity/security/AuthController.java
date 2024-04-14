package com.chris.ims.entity.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/auth")
class AuthController {

  public record LoginRequest(
      @NotNull(message = "username is required")
      @NotBlank(message = "username can't be blank")
      String username,

      @NotNull(message = "password is required")
      @NotBlank(message = "password can't be blank")
      String password
  ) implements Serializable {
  }

  public record JwtDto(
      String token
  ) implements Serializable {
  }

  private final AuthService service;

  @PostMapping("/login")
  public JwtDto login(@Valid @RequestBody LoginRequest request) {
    return service.login(request);
  }
}
