package org.nuvola.mobile.prixpascher.dto;

public class AlertEmailVO {
    private String email;
    private Double targetPrice;

    public AlertEmailVO() {
    }

    public AlertEmailVO(String email, Double targetPrice) {
        this.email = email;
        this.targetPrice = targetPrice;
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
