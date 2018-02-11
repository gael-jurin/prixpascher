package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.ProductSource;
import org.nuvola.mobile.prixpascher.models.ShopType;
import org.nuvola.mobile.prixpascher.models.Taskable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductVO implements Serializable, Taskable {
    private String id;
    private ShopType shopType;
    private String shopInfoBannerScript;
    private String age;
    private String shopName;
    private String title;
    private Double price;
    private String link;
    private String affiliatedLink;
    private String image;
    private Category category;
    private String categoryTitle;
    private Category subCategory;
    private String categoryName;
    private String productCategory;
    private ProductSource source;
    private Boolean promoted;
    private Long views;
    private Long quantity;
    private Date viewed;
    private Date trackingDate;
    private String detail;
    private String specification;
    private List<ReviewVO> reviews;
    private List<PriceHistoryVO> prices;
    private List<AlertEmailVO> alerts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<PriceHistoryVO> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceHistoryVO> prices) {
        this.prices = prices;
    }

    public List<ReviewVO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewVO> reviews) {
        this.reviews = reviews;
    }

    public List<AlertEmailVO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertEmailVO> alerts) {
        this.alerts = alerts;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Date getViewed() {
        return viewed;
    }

    public void setViewed(Date viewed) {
        this.viewed = viewed;
    }

    public Date getTrackingDate() {
        return trackingDate;
    }

    public void setTrackingDate(Date trackingDate) {
        this.trackingDate = trackingDate;
    }

    public Boolean getPromoted() {
        return promoted;
    }

    public void setPromoted(Boolean promoted) {
        this.promoted = promoted;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }

    public String getShopInfoBannerScript() {
        return shopInfoBannerScript;
    }

    public void setShopInfoBannerScript(String shopInfoBannerScript) {
        this.shopInfoBannerScript = shopInfoBannerScript;
    }

    public String getAffiliatedLink() {
        return affiliatedLink;
    }

    public void setAffiliatedLink(String affiliatedLink) {
        this.affiliatedLink = affiliatedLink;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getShopName() {
        shopName = "";
        if (getShopType() != null) {
            shopName = getShopType().name().toLowerCase();
        }
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCategoryName() {
        categoryName = "";
        if (getSubCategory() != null) {
            categoryName = getSubCategory().name().toLowerCase();
            if (categoryName.contains("homme_") || categoryName.contains("femme_")) {
                categoryName = categoryName.split("_")[1] + " " + categoryName.split("_")[0];
            } else {
                categoryName = categoryName.replace("_", " ");
            }
        }
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductCategory() {
        categoryName = "";
        if (this.category != null) {
            categoryName = this.category.name().toLowerCase();
            categoryName = categoryName.replace("_", " ");
        }
        return categoryName;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public ProductSource getSource() {
        return source;
    }

    public void setSource(ProductSource source) {
        this.source = source;
    }
}
