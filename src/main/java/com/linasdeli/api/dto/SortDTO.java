package com.linasdeli.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortDTO {
    private boolean empty;
    private boolean unsorted;
    private boolean sorted;
}
