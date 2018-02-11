package org.nuvola.mobile.prixpascher.dto;

import org.nuvola.mobile.prixpascher.models.Taskable;

import java.util.Date;

public class ReviewVO implements Taskable {
    private String id;
    private UserVO user;
    private String productId;
    private Date date;
    private Integer mNumStars;
    private String comment;

    public ReviewVO() {
        this.mNumStars = 0;
        this.date = new Date();
        this.comment="";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getmNumStars() {
        return mNumStars;
    }

    public void setmNumStars(Integer mNumStars) {
        this.mNumStars = mNumStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
