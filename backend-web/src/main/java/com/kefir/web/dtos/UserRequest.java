package com.kefir.web.dtos;

import com.kefir.enums.UserRoles;
import java.util.List;

public record UserRequest(
    String username, String fullname, String password, List<UserRoles> roles) {}
