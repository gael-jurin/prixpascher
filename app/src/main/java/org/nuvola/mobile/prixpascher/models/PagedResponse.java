package org.nuvola.mobile.prixpascher.models;

import java.util.List;

public interface PagedResponse {
    List getPayload();

    Long getTotalElements();

    void setTotalElements(Long totalElements);
}
