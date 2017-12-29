package org.nuvola.mobile.prixpascher.models;

public enum Authority {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private String label;

    Authority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
