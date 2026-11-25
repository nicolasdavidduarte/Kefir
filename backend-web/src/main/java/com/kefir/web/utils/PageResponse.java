package com.kefir.web.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
    List<T> content, int page, int size, Long totalElements, int totalPages, Boolean last) {
  public PageResponse {
    content = content == null ? List.of() : List.copyOf(content);
  }

  public static <T> PageResponse<T> from(Page<T> page) {
    Objects.requireNonNull(page, "page must not be null");
    int pageNumber = page.getPageable().isPaged() ? page.getNumber() + 1 : 1;
    int pageSize = page.getPageable().isPaged() ? page.getSize() : page.getNumberOfElements();

    return new PageResponse<>(
        page.getContent(),
        pageNumber,
        pageSize,
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast());
  }

  public static <E, D> PageResponse<D> from(Page<E> page, Function<E, D> mapper) {
    Objects.requireNonNull(page, "page must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");
    return from(page.map(mapper));
  }
}
