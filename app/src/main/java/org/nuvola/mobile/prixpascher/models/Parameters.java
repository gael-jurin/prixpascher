package org.nuvola.mobile.prixpascher.models;

import java.util.List;

public class Parameters<T> {
    private List<T> payload;

    public Parameters() {
    }

    public Parameters(List<T> payload) {
        this.payload = payload;
    }

    public List<T> getPayload() {
        return payload;
    }

    public void setPayload(List<T> payload) {
        this.payload = payload;
    }
}
