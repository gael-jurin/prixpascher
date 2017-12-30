package org.nuvola.mobile.prixpascher.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.nuvola.mobile.prixpascher.models.OfferStatus;
import org.nuvola.mobile.prixpascher.models.Taskable;

import java.io.Serializable;
import java.util.Date;

public class OfferVO implements Serializable, Taskable {
    private String offerId;
    private String email;
    private Date posted;
    private OfferStatus offerStatus;
    private String targetPrice;
    private ShopInfoVO shop;
    private ProductAnnonceVO productAnnonce;
    private String stock;
    private String attachedDevis;
    private String codePromo;


    public OfferVO() {
    }

    public OfferVO(String email, String targetPrice) {
        this.email = email;
        this.targetPrice = targetPrice;
        this.offerStatus = OfferStatus.SUBMITTED;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
    }

    public ShopInfoVO getShop() {
        return shop;
    }

    public void setShop(ShopInfoVO shop) {
        this.shop = shop;
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

    @Override
    @JsonIgnore
    public String getId() {
        return getOfferId();
    }
}
