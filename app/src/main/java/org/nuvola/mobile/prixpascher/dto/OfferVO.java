package org.nuvola.mobile.prixpascher.dto;

import java.util.Date;

public class OfferVO {
    private String email;
    private Date posted;
    private Boolean valid;
    private String targetPrice;
    private ProductAnnonceVO productAnnonce;
    private String stock;
    private String attachedDevis;
    private String codePromo;


    public OfferVO() {
    }

    public OfferVO(String email, String targetPrice) {
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

    public ProductAnnonceVO getProductAnnonce() {
        return productAnnonce;
    }

    public void setProductAnnonce(ProductAnnonceVO productAnnonce) {
        this.productAnnonce = productAnnonce;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCodePromo() {
        return codePromo;
    }

    public void setCodePromo(String codePromo) {
        this.codePromo = codePromo;
    }

    public String getAttachedDevis() {
        return attachedDevis;
    }

    public void setAttachedDevis(String attachedDevis) {
        this.attachedDevis = attachedDevis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(String targetPrice) {
        this.targetPrice = targetPrice;
    }
}
