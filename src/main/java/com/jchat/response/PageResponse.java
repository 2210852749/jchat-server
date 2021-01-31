package com.jchat.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {

  private List<T> data;
  private int total;
  private int code;
  private String message;

  public static <T> PageResponse<T> success(List<T> data, int total) {
    return new PageResponse<>(data, total, 0, null);
  }
}
