package org.nuvola.mobile.prixpascher.dto;

import java.util.Date;

public class AlertEmailVO {
    private String email;
    private Double targetPrice;
    private Date created;
    private Date executed;

    public AlertEmailVO() {
    }

    public AlertEmailVO(String email, Double targetPrice) {
        this.email = email;
        this.targetPrice = targetPrice;
        this.created = new Date();
        this.executed = new Date();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExecuted() {
        return executed;
    }

    public void setExecuted(Date executed) {
        this.executed = executed;
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
