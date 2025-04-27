package com.linasdeli.api.dto.response;

import com.linasdeli.api.domain.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CustomerProductListDTO {
    private Integer pid;
    private String productImageName;
    private String productImageUrl;
    private String productName;
    private String originName;
    private String categoryName;
    private String animalName;
    private Boolean pasteurized;
    private List<AllergyType> allergies;
}
