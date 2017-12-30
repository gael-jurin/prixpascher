package org.nuvola.mobile.prixpascher.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum OfferStatus {
    SUBMITTED("En validation"),
    VALIDATED("Validée"),
    CHALLENGED("En concurrence"),
    ACCEPTED("Acceptée"),
    CLOSED("Cloturée");

    private String label;

    OfferStatus(String label) {
        this.label = label;
    }


    public static List<OfferStatus> offStatus() {
        List offers = new ArrayList();
        offers.addAll(Collections.singletonList(ACCEPTED));
        offers.addAll(Collections.singletonList(CLOSED));
        return offers;
    }

    public static List<OfferStatus> processingStatus() {
        List offers = new ArrayList();
        offers.addAll(Collections.singletonList(SUBMITTED));
        offers.addAll(Collections.singletonList(VALIDATED));
        offers.addAll(Collections.singletonList(CHALLENGED));
        return offers;
    }
}
