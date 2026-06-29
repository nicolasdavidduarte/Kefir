package com.kefir.services;

import com.kefir.entities.Role;
import com.kefir.entities.User;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.UserRepository;
import com.kefir.web.dtos.common.EntityStatusUpdate;
import com.kefir.web.dtos.user.UserRequest;
import com.kefir.web.dtos.user.UserResponse;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final AuthService authService;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      AuthService authService,
      RoleService roleService,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.authService = authService;
    this.roleService = roleService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getAll() {
    return userRepository.findAllByOrderByIdAsc().stream().map(UserResponse::fromEntity).toList();
  }

  @Transactional(readOnly = true)
  public UserResponse getByIdWithResponse(Integer id) {
    return userRepository
        .findById(id)
        .map(UserResponse::fromEntity)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public User getById(Integer id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public UserResponse create(UserRequest request) {
    Set<Role> roles = roleService.getRolesByName(request.roles());

    User userCreator = getById(authService.getCurrentUserId());

    User newUser =
        User.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .fullName(request.fullname())
            .enabled(false)
            .userId(userCreator)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .roles(roles)
            .build();

    User userSaved = userRepository.save(newUser);

    return UserResponse.fromEntity(userSaved);
  }

  @Transactional
  public String updateStatus(Integer id, EntityStatusUpdate status) {

    User user =
        userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    user.setEnabled(status.enabled());

    userRepository.save(user);

    return (status.enabled()) ? "User successfully activated" : "User successfully deactivated";
  }
}
