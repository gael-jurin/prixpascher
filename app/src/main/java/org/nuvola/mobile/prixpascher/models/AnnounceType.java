package org.nuvola.mobile.prixpascher.models;

import java.util.ArrayList;
import java.util.List;

public enum AnnounceType {
    COMMON_SELL("Vente"),
    COMMON_BUY("Achat"),
    IMO_SELL("Immobillier Vente"),
    IMO_LOCATION("Immobillier Location"),
    AUTO_SELL("Voiture Vente"),
    AUTO_BUY("Voiture Achat"),
    SERVICE_PRO("Fournisseur Service"),
    SERVICE_ASK("Demande Service"),
    OFFER_ASK("Demande Devis");

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

    public static List<AnnounceType> typesAchat() {
        List<AnnounceType> typesAchat = new ArrayList();
        typesAchat.add(AnnounceType.COMMON_BUY);
        typesAchat.add(AnnounceType.AUTO_BUY);
        return typesAchat;
    }

    public static List<AnnounceType> typesVente() {
        List<AnnounceType> typesAchat = new ArrayList();
        typesAchat.add(AnnounceType.COMMON_SELL);
        typesAchat.add(AnnounceType.AUTO_SELL);
        return typesAchat;
    }

    public static List<AnnounceType> typesService() {
        List<AnnounceType> typesAchat = new ArrayList();
        typesAchat.add(AnnounceType.OFFER_ASK);
        typesAchat.add(AnnounceType.SERVICE_ASK);
        typesAchat.add(AnnounceType.SERVICE_PRO);
        return typesAchat;
    }

    public static List<AnnounceType> typesImmo() {
        List<AnnounceType> typesAchat = new ArrayList();
        typesAchat.add(AnnounceType.IMO_SELL);
        typesAchat.add(AnnounceType.IMO_LOCATION);
        return typesAchat;
    }
}
