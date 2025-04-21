package com.linasdeli.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryCountDTO {
    private String categoryName;
    private long count;
}