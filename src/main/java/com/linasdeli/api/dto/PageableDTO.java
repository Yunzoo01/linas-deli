package com.linasdeli.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableDTO {
    private int pageNumber;
    private int pageSize;
    private SortDTO sort;
    private int offset;
    private boolean unpaged;
    private boolean paged;
}