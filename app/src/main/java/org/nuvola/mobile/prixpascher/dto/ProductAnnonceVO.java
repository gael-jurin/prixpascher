package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.AnnounceStatus;
import org.nuvola.mobile.prixpascher.models.AnnounceType;
import org.nuvola.mobile.prixpascher.models.Category;
import org.nuvola.mobile.prixpascher.models.City;
import org.nuvola.mobile.prixpascher.models.ProductSource;

import java.util.Date;

public class ProductAnnonceVO {
    private String id;
    private String parentId;
    private String feedId;
    private String userGroupChain;
    private Date trackingDate;
    private String title;
    private Double price;
    private String link;
    private String image;
    private String image1;
    private String image2;
    private String image3;
    private AnnounceStatus status;
    private AnnounceType type;
    private Category category;
    private City ville;
    private String contactVille;
    private String contactVoie;
    private String contactPhone;
    private String contactMail;
    private ProductSource source;
    private Long views;
    private Date viewed;
    private String detail;
    private String specification;
    // private List<OfferVO> offers;
    private UserVO user;
    private String cityName;
    private String categoryName;

    public ProductAnnonceVO() {
        this.source = ProductSource.DEFAULT;
    }

    public ProductAnnonceVO(ProductVO product) {
        this.parentId = product.getId();
        this.title = product.getTitle();
        this.source = ProductSource.ANNONCE;
        this.price = product.getPrice();
        this.trackingDate = new Date();
        this.category = product.getCategory();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTrackingDate() {
        return trackingDate;
    }

    public void setTrackingDate(Date trackingDate) {
        this.trackingDate = trackingDate;
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

    public AnnounceStatus getStatus() {
        return status;
    }

    public void setStatus(AnnounceStatus status) {
        this.status = status;
    }

    public AnnounceType getType() {
        return type;
    }

    public void setType(AnnounceType type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public City getVille() {
        return ville;
    }

    public void setVille(City ville) {
        this.ville = ville;
    }

    public String getContactVille() {
        return contactVille;
    }

    public void setContactVille(String contactVille) {
        this.contactVille = contactVille;
    }

    public String getContactVoie() {
        return contactVoie;
    }

    public void setContactVoie(String contactVoie) {
        this.contactVoie = contactVoie;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getUserGroupChain() {
        return userGroupChain;
    }

    public void setUserGroupChain(String userGroupChain) {
        this.userGroupChain = userGroupChain;
    }

    /*public List<OfferVO> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferVO> offers) {
        this.offers = offers;
    }*/

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

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductSource getSource() {
        return source;
    }

    public void setSource(ProductSource source) {
        this.source = source;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public String getCityName() {
        cityName = "";
        if (getContactVille() != null) {
            cityName = getContactVille().toLowerCase();
        }
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCategoryName() {
        categoryName = "";
        if (getCategory() != null) {
            categoryName = getCategory().name().toLowerCase();
            categoryName = categoryName.replace("_", " ");
        }
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
