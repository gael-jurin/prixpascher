package org.nuvola.mobile.prixpascher.models;

import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;

import java.util.ArrayList;
import java.util.List;

public class AnnouncesResponse implements PagedResponse {
    private List<ProductAnnonceVO> payload;
    private Long totalElements;

    public AnnouncesResponse() {
        totalElements = 0L;
        payload = new ArrayList<>();
    }

    public AnnouncesResponse(List<ProductAnnonceVO> payload) {
        this.payload = payload;
    }

    public AnnouncesResponse(List<ProductAnnonceVO> payload, Long totalElements) {
        this.payload = payload;
        this.totalElements = totalElements;
    }

    public List<ProductAnnonceVO> getPayload() {
        return payload;
    }

    public void setPayload(List<ProductAnnonceVO> payload) {
        this.payload = payload;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
