package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.ShopType;

import java.util.List;

public class SearchContextVO {
    private Double minPrice;
    private Double maxPrice;
    private List<ShopType> shopTypes;

    public SearchContextVO() {
    }

    public SearchContextVO(Double minPrice,
                           Double maxPrice,
                           List<ShopType> shopTypes) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.shopTypes = shopTypes;
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

    public List<ShopType> getShopTypes() {
        return shopTypes;
    }

    public void setShopTypes(List<ShopType> shopTypes) {
        this.shopTypes = shopTypes;
    }
}
