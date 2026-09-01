package com.kefir.web.utils;

import com.kefir.exceptions.InvalidPaginationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public final class Pagination {

  public static Pageable from(Integer page, Integer size) {
    if (page == null && size == null) {
      return Pageable.unpaged();
    }

    if (page == null || size == null) {
      throw new InvalidPaginationException("Page and size must be provided together");
    }

    if (page < 1) {
      throw new InvalidPaginationException("Page must be greater than 0");
    }

    if (size < 1) {
      throw new InvalidPaginationException("Size must be greater than 0");
    }

    if (size > 20) {
      throw new InvalidPaginationException("Size must not exceed 20");
    }

    return PageRequest.of(page - 1, size);
  }
}
