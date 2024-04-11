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
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());

    return userFacade.save(user).toDto();
  }

  public UserDto getUser(Long id) {
    return userFacade.findById(id).toDto();
  }

  public UserDto updateUser(Long id, UserRequest request) {
    User user = userFacade.findById(id).edit();
    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());

    return userFacade.save(user).toDto();
  }

  public UserDto patchUser(Long id, UserRequest request) {
    User user = userFacade.findById(id).edit();

    if (request.getName() != null)
      user.setName(request.getName());
    if (request.getUsername() != null)
      user.setUsername(request.getUsername());
    if (request.getPassword() != null)
      user.setPassword(request.getPassword());

    return userFacade.save(user).toDto();
  }

  public UserDto deleteUser(Long id) {
    User user = userFacade.findById(id);

    return userFacade.delete(user).toDto();
  }
}
