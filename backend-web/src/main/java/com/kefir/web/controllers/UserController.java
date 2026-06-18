package com.kefir.web.controllers;

import com.kefir.services.UserService;
import com.kefir.web.dtos.common.ApiEntityResponse;
import com.kefir.web.dtos.common.EntityStatusUpdate;
import com.kefir.web.dtos.user.UserRequest;
import com.kefir.web.dtos.user.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "User services", description = "APIs for system users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponse> getAll() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse getById(@PathVariable Integer id) {
    return userService.getByIdWithResponse(id);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public UserResponse createUser(@RequestBody @Valid UserRequest request) {
    return userService.create(request);
  }

  @PostMapping("/{id}/status")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ApiEntityResponse updateStatus(
      @PathVariable Integer id, @RequestBody EntityStatusUpdate status) {
    String okMessage = userService.updateStatus(id, status);

    return new ApiEntityResponse(id.longValue(), okMessage, OffsetDateTime.now());
  }
}
