package com.linasdeli.api.dto.response;


import com.linasdeli.api.domain.enums.AllergyType;

import java.util.List;

public class ProductWithDetailsDto {
    private Integer pid;
    private String imageName;
    private String imageUrl;
    private String productName;
    private String description;
    private Boolean pasteurized;
    private String ingredientsImageName;
    private String ingredientsImageUrl;
    private String categoryName;
    private String animalName;
    private List<AllergyType> allergies;
    private String countryName;

    public ProductWithDetailsDto(Integer pid,
                                 String imageName,
                                 String imageUrl,
                                 String productName,
                                 String description,
                                 Boolean pasteurized,
                                 String ingredientsImageName,
                                 String ingredientsImageUrl,
                                 String categoryName,
                                 String animalName,
                                 List<AllergyType> allergies,
                                 String countryName) {
        this.pid = pid;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.description = description;
        this.pasteurized = pasteurized;
        this.ingredientsImageName = ingredientsImageName;
        this.ingredientsImageUrl = ingredientsImageUrl;
        this.categoryName = categoryName;
        this.animalName = animalName;
        this.allergies = allergies;
        this.countryName = countryName;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPasteurized() {
        return pasteurized;
    }

    public void setPasteurized(Boolean pasteurized) {
        this.pasteurized = pasteurized;
    }

    public String getIngredientsImageName() {
        return ingredientsImageName;
    }

    public void setIngredientsImageName(String ingredientsImageName) {
        this.ingredientsImageName = ingredientsImageName;
    }

    public String getIngredientsImageUrl() {
        return ingredientsImageUrl;
    }

    public void setIngredientsImageUrl(String ingredientsImageUrl) {
        this.ingredientsImageUrl = ingredientsImageUrl;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<AllergyType> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<AllergyType> allergies) {
        this.allergies = allergies;
    }
}
