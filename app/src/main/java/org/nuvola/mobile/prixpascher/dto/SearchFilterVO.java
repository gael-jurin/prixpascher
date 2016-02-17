package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.AnnounceType;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.City;
import org.nuvola.mobile.prixpascher.models.Filter;

import java.util.List;

public class SearchFilterVO extends Filter {
    private String brand;
    private String userId;
    private City city;
    private AnnounceType type;
    private Double minPrice;
    private Double maxPrice;
    private String searchText;
    private List<String> brands;
    private Category category;
    private Boolean isSubCategory;
    private Boolean promotion;
    private Boolean enValidation;

    public SearchFilterVO() {
        isSubCategory = false;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Boolean getEnValidation() {
        return enValidation;
    }

    public void setEnValidation(Boolean enValidation) {
        this.enValidation = enValidation;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getIsSubCategory() {
        return isSubCategory;
    }

    public void setIsSubCategory(Boolean isSubCategory) {
        this.isSubCategory = isSubCategory;
    }

    public AnnounceType getType() {
        return type;
    }

    public void setType(AnnounceType type) {
        this.type = type;
    }

    public Boolean getPromotion() {
        return promotion;
    }

    public void setPromotion(Boolean promotion) {
        this.promotion = promotion;
    }
}
