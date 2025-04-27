package com.linasdeli.api.dto.response;

import com.linasdeli.api.domain.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CustomerProductDTO {
    private Integer pid;
    private String productName;
    private String originName;
    private List<AllergyType> allergies;
    private String description;
    private Boolean pasteurized;
    private String servingSuggestion;
    private String ingredientsImageName;
    private String ingredientsImageUrl;
}