package org.nuvola.mobile.prixpascher.models;

public enum SortField {
    PRICE("price"),
    POPULAR("score"),
    MOST_VIEWED("views"),
    MOST_UPDATED("trackingDate");

    private String key;

    SortField(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Boolean isOfType(SortField... types) {
        for (SortField sortField : types) {
            if (this == sortField) {
                return true;
            }
        }
        return false;
    }
}
