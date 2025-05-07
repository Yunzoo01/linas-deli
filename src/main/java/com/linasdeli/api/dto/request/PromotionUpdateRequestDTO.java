package com.linasdeli.api.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class PromotionUpdateRequestDTO {
    private String promotionTitle;
    private String startDate; // ✅ LocalDateTime → String
    private String endDate;   // ✅ LocalDateTime → String
    private MultipartFile image;
}