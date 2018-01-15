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
    private String infos;
    private Long stock;
    private String codePromo;
    private Double targetPrice;
    private String attachedDevis;
    private OfferStatus offerStatus;
    private UserVO annonceur;
    private ShopInfoVO shop;
    private ProductAnnonceVO productAnnonce;

    public OfferVO() {
    }

    public OfferVO(String email, Double targetPrice) {
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

    public UserVO getAnnonceur() {
        return annonceur;
    }

    public void setAnnonceur(UserVO annonceur) {
        this.annonceur = annonceur;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
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

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
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

    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return getOfferId();
    }
}
