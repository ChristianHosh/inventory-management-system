package com.chris.ims.entity.user;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/users")
class UserController {

  private final UserService service;

  @GetMapping("")
  public Page<UserDto> getUsers(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query
  ) {
    return service.getUsers(page, size, query);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  public UserDto createUser(@Valid @RequestBody UserRequest request) {
    return service.createUser(request);
  }

  @GetMapping("/{id}")
  public UserDto getUser(@PathVariable Long id) {
    return service.getUser(id);
  }

  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
    return service.updateUser(id, request);
  }

  @PatchMapping("/{id}")
  @Validated(RequireAll.class)
  public UserDto patchUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
    return service.patchUser(id, request);
  }

  @DeleteMapping("/{id}")
  public UserDto deleteUser(@PathVariable Long id) {
    return service.deleteUser(id);
  }
}
