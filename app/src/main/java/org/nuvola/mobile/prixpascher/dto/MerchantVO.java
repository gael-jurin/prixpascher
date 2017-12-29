package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.ShopType;

import java.util.ArrayList;
import java.util.List;

public class MerchantVO {
    private ShopType shopType;
    private ShopInfoVO shopInfoVO;
    private Long productsCount;
    private Long viewsCount;
    private List<ProductVO> mostVisitedProducts;

    public MerchantVO() {
        mostVisitedProducts = new ArrayList<>();
        productsCount = 0L;
        viewsCount = 0L;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }

    public ShopInfoVO getShopInfoVO() {
        return shopInfoVO;
    }

    public void setShopInfoVO(ShopInfoVO shopInfoVO) {
        this.shopInfoVO = shopInfoVO;
    }

    public Long getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(Long productsCount) {
        this.productsCount = productsCount;
    }

    public Long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public List<ProductVO> getMostVisitedProducts() {
        return mostVisitedProducts;
    }

    public void setMostVisitedProducts(List<ProductVO> mostVisitedProducts) {
        this.mostVisitedProducts = mostVisitedProducts;
    }
}
