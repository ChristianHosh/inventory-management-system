package com.chris.ims.entity.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserService {

  private final UserFacade userFacade;

  public Page<UserDto> getUsers(int page, int size, String query) {
    return userFacade.searchQuery(query, page, size).map(User::toDto);
  }

  public UserDto createUser(UserRequest request) {
    User user = userFacade.newEntity(request);
    user.setField(User.F_USERNAME, request.getUsername());
    user.setField(User.F_PASSWORD, request.getPassword());

    return userFacade.save(user).toDto();
  }

  public UserDto getUser(Long id) {
    return userFacade.findById(id).toDto();
  }

  public UserDto updateUser(Long id, UserRequest request) {
    User user = userFacade.findById(id).edit();
    user.setField(User.F_NAME, request.getName());
    user.setField(User.F_USERNAME, request.getUsername());
    user.setField(User.F_PASSWORD, request.getPassword());

    return userFacade.save(user).toDto();
  }

  public UserDto patchUser(Long id, UserRequest request) {
    User user = userFacade.findById(id).edit();

    if (request.getName() != null)
      user.setField(User.F_NAME, request.getName());
    if (request.getUsername() != null)
      user.setField(User.F_USERNAME, request.getUsername());
    if (request.getPassword() != null)
      user.setField(User.F_PASSWORD, request.getPassword());

    return userFacade.save(user).toDto();
  }

  public UserDto deleteUser(Long id) {
    User user = userFacade.findById(id);

    return userFacade.delete(user).toDto();
  }
}
