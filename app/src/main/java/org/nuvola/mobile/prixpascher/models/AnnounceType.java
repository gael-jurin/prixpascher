package org.nuvola.mobile.prixpascher.models;

public enum AnnounceType {
    COMMON_SELL("Vente"),
    COMMON_BUY("Achat"),
    IMO_SELL("Immobillier Vente"),
    IMO_LOCATION("Immobillier Location"),
    AUTO_SELL("Voiture Vente"),
    AUTO_BUY("Voiture Achat"),
    SERVICE_PRO("Fournisseur Service"),
    SERVICE_ASK("Demande Service");

    private String label;

    AnnounceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
