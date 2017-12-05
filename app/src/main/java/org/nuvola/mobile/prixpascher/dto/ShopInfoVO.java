package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.ShopStatus;
import org.nuvola.mobile.prixpascher.models.ShopType;

import java.util.Date;

public class ShopInfoVO {
    private String id;
    private String primaryAccountUserName;
    private String primaryAccountEmail;
    private String name;
    private Date updated;
    private ShopType classe;
    private ShopStatus status;
    private String description;
    private String website;
    private String bannerImg;
    private String bannerScript;
    private String feed;
    private Boolean afilliate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrimaryAccountUserName() {
        return primaryAccountUserName;
    }

    public void setPrimaryAccountUserName(String primaryAccountUserName) {
        this.primaryAccountUserName = primaryAccountUserName;
    }

    public String getPrimaryAccountEmail() {
        return primaryAccountEmail;
    }

    public void setPrimaryAccountEmail(String primaryAccountEmail) {
        this.primaryAccountEmail = primaryAccountEmail;
    }

    public ShopStatus getStatus() {
        return status;
    }

    public void setStatus(ShopStatus status) {
        this.status = status;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerScript() {
        return bannerScript;
    }

    public void setBannerScript(String bannerScript) {
        this.bannerScript = bannerScript;
    }

    public Boolean getAfilliate() {
        return afilliate;
    }

    public void setAfilliate(Boolean afilliate) {
        this.afilliate = afilliate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public ShopType getClasse() {
        return classe;
    }

    public void setClasse(ShopType classe) {
        this.classe = classe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
