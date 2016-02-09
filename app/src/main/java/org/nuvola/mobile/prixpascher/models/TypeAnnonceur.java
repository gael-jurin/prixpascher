package org.nuvola.mobile.prixpascher.models;

public enum TypeAnnonceur {
    PRIVATE("Particulier"),
    PROFESSIONAL("Professionel");

    private String label;

    TypeAnnonceur(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Boolean isOfType(TypeAnnonceur... types) {
        for (TypeAnnonceur type : types) {
            if (this == type) {
                return true;
            }
        }
        return false;
    }
}
