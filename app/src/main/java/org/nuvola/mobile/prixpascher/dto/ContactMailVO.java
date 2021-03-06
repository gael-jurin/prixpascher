package org.nuvola.mobile.prixpascher.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.nuvola.mobile.prixpascher.models.Taskable;

import java.io.Serializable;

public class ContactMailVO implements Serializable, Taskable {
    private String email;
    private String subject;
    private String message;
    private OfferVO offer;

    public ContactMailVO() {
        this.offer = new OfferVO();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public OfferVO getOffer() {
        return offer;
    }

    public void setOffer(OfferVO offer) {
        this.offer = offer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return email;
    }
}
