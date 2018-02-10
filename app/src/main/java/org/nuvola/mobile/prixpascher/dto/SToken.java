package org.nuvola.mobile.prixpascher.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.nuvola.mobile.prixpascher.models.Taskable;

public class SToken implements Taskable {
    private String serialToken;

    public String getSerialToken() {
        return serialToken;
    }

    public void setSerialToken(String serialToken) {
        this.serialToken = serialToken;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return serialToken;
    }
}
