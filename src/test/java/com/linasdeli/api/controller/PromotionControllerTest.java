package com.linasdeli.api.controller;

import com.linasdeli.api.domain.Promotion;
import com.linasdeli.api.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("🧪 REST API - Promotion Controller")
public class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(promotionController).build();
    }

    @Test
    void givenValidPromotionDetails_whenCreatePromotion_thenReturnCreatedPromotion() throws Exception {
        // Given: Valid promotion details
        String promotionTitle = ""; // 또는 null: 자동 생성 테스트용
        LocalDateTime startDate = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 3, 31, 23, 59);

        Promotion promotion = new Promotion();
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setPromotionTitle("Promotion from 2025-03-01 to 2025-03-31");

        // When: createPromotion 호출 예상 설정
        when(promotionService.createPromotion(eq(promotionTitle), eq(startDate), eq(endDate), isNull()))
                .thenReturn(promotion);

        // Then: 요청 보내고 결과 확인
        mockMvc.perform(post("/api/staff/promotions")
                        .param("promotionTitle", promotionTitle)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.promotionTitle").value("Promotion from 2025-03-01 to 2025-03-31"))
                .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate.toString()));

        // Verify: promotionService가 호출됐는지 확인
        verify(promotionService, times(1))
                .createPromotion(eq(promotionTitle), eq(startDate), eq(endDate), isNull());
    }

    @Test
    void givenExistingPromotionId_whenGetPromotionById_thenReturnPromotion() throws Exception {
        // Given: Existing promotion ID
        Long promotionId = 1L;
        LocalDateTime startDate = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 3, 31, 23, 59);
        Promotion promotion = new Promotion();
        promotion.setPromotionId(promotionId);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setPromotionTitle("Promotion from 2025-03-01 to 2025-03-31");

        // When: Get promotion by ID
        when(promotionService.getPromotionById(promotionId)).thenReturn(promotion);

        // Then: The promotion should be returned with status 200
        mockMvc.perform(get("/api/promotions/{id}", promotionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promotionTitle").value("Promotion from 2025-03-01 to 2025-03-31"))
                .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate.toString()));

        verify(promotionService, times(1)).getPromotionById(promotionId);
    }

    @Test
    void givenKeyword_whenGetPromotions_thenReturnPagedPromotions() throws Exception {
        // Given: A keyword for search and a list of promotions
        LocalDateTime startDate = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 3, 31, 23, 59);
        Promotion promotion = new Promotion();
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setPromotionTitle("Promotion from 2025-03-01 to 2025-03-31");

        // When: Get promotions with search keyword
        when(promotionService.getPromotions(any(), eq("Promotion"))).thenReturn(new PageImpl<>(List.of(promotion)));

        // Then: The list of promotions should be returned with status 200
        mockMvc.perform(get("/api/promotions")
                        .param("keyword", "Promotion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].promotionTitle").value("Promotion from 2025-03-01 to 2025-03-31"));

        verify(promotionService, times(1)).getPromotions(any(), eq("Promotion"));
    }

    @Test
    void givenValidPromotionIdAndDetails_whenUpdatePromotion_thenReturnUpdatedPromotion() throws Exception {
        // Given: Valid promotion ID and updated details
        Long promotionId = 1L;
        LocalDateTime startDate = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 3, 31, 23, 59);
        Promotion promotion = new Promotion();
        promotion.setPromotionId(promotionId);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setPromotionTitle("Updated Promotion");

        // When: Update the promotion
        when(promotionService.updatePromotion(promotionId, startDate, endDate, null)).thenReturn(promotion);

        // Then: The updated promotion should be returned with status 200
        mockMvc.perform(put("/api/promotions/{id}", promotionId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promotionTitle").value("Updated Promotion"));

        verify(promotionService, times(1)).updatePromotion(promotionId, startDate, endDate, null);
    }

    @Test
    void givenValidPromotionId_whenDeletePromotion_thenReturnNoContent() throws Exception {
        // Given: Valid promotion ID
        Long promotionId = 1L;

        // When: Delete promotion by ID
        mockMvc.perform(delete("/api/promotions/{id}", promotionId))
                .andExpect(status().isNoContent());

        // Then: The promotion should be deleted with status 204
        verify(promotionService, times(1)).deletePromotion(promotionId);
    }
}
