package com.example.mody.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CursorPagination {
    private Boolean hasNext;
    private Long cursor;
}
