package org.nuvola.mobile.prixpascher.dto;

import java.util.Date;

public class OfferVO {
    private String email;
    private Date posted;
    private Boolean valid;
    private Double targetPrice;
    private String productAnnonceProductId;
    private String productAnnonceTitle;
    private Double productAnnoncePrice;

    public OfferVO() {
    }

    public OfferVO(String email, Double targetPrice) {
        this.email = email;
        this.targetPrice = targetPrice;
        setValid(false);
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getProductAnnonceProductId() {
        return productAnnonceProductId;
    }

    public void setProductAnnonceProductId(String productAnnonceProductId) {
        this.productAnnonceProductId = productAnnonceProductId;
    }

    public String getProductAnnonceTitle() {
        return productAnnonceTitle;
    }

    public void setProductAnnonceTitle(String productAnnonceTitle) {
        this.productAnnonceTitle = productAnnonceTitle;
    }

    public Double getProductAnnoncePrice() {
        return productAnnoncePrice;
    }

    public void setProductAnnoncePrice(Double productAnnoncePrice) {
        this.productAnnoncePrice = productAnnoncePrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }
}
