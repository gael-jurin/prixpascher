package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.Authority;
import org.nuvola.mobile.prixpascher.models.ShopType;
import org.nuvola.mobile.prixpascher.models.TypeAnnonceur;

import java.util.Date;

public class UserVO {
    private String id;
    private String userName;
    private String email;
    private Authority authority;
    private String firstName;
    private String lastName;
    private String providerUserId;
    private String accessToken;
    private String photo;
    private String primaryShop;
    private String primaryShopId;
    private ShopType primaryShopClasse;
    private TypeAnnonceur typeAnnonceur;
    private String userGroupChain;
    private Date joined;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrimaryShop() {
        return primaryShop;
    }

    public void setPrimaryShop(String primaryShop) {
        this.primaryShop = primaryShop;
    }

    public ShopType getPrimaryShopClasse() {
        return primaryShopClasse;
    }

    public void setPrimaryShopClasse(ShopType primaryShopClasse) {
        this.primaryShopClasse = primaryShopClasse;
    }

    public TypeAnnonceur getTypeAnnonceur() {
        return typeAnnonceur;
    }

    public void setTypeAnnonceur(TypeAnnonceur typeAnnonceur) {
        this.typeAnnonceur = typeAnnonceur;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public String getUserGroupChain() {
        return userGroupChain;
    }

    public void setUserGroupChain(String userGroupChain) {
        this.userGroupChain = userGroupChain;
    }

    public void setPrimaryShopId(String primaryShopId) {
        this.primaryShopId = primaryShopId;
    }

    public String getPrimaryShopId() {
        return primaryShopId;
    }
}
