package com.linasdeli.api.dto.response;

import com.linasdeli.api.dto.SupplierDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class SupplierResponseDTO {
    private Page<SupplierDTO> suppliers;
    private long totalSupplierCount;

    public SupplierResponseDTO(Page<SupplierDTO> suppliers, long totalSupplierCount) {
        this.suppliers = suppliers;
        this.totalSupplierCount = totalSupplierCount;
    }
}
