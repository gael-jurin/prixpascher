package org.nuvola.mobile.prixpascher.models;

import java.util.ArrayList;
import java.util.List;

public enum AnnounceStatus {
    SUBMITTED("Postée"),
    VALIDATED("Validée"),
    CLOSED("Cloturée"),
    DELETED("Archivée"),
    UNCHANGED("Non modifiée");

    private String label;

    AnnounceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static List<AnnounceStatus> userAnnounceStatus() {
        List<AnnounceStatus> userAnnounceStatus = new ArrayList<>();
        userAnnounceStatus.add(VALIDATED);
        userAnnounceStatus.add(CLOSED);
        return userAnnounceStatus;
    }
}
