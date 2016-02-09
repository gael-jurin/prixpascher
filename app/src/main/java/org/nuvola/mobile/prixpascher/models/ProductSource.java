package org.nuvola.mobile.prixpascher.models;

public enum ProductSource {
    DEFAULT("default"),
    ANNONCE("Annonce");

    private String label;

    ProductSource(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Boolean isOfType(ProductSource... types) {
        for (ProductSource type : types) {
            if (this == type) {
                return true;
            }
        }
        return false;
    }
}
