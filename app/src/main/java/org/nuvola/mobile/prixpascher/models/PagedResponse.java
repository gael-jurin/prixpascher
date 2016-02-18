package org.nuvola.mobile.prixpascher.models;

import org.nuvola.mobile.prixpascher.dto.ProductVO;

import java.util.ArrayList;
import java.util.List;

public class PagedResponse {
    private List<ProductVO> payload;
    private Long totalElements;

    public PagedResponse() {
        totalElements = 0L;
        payload = new ArrayList<>();
    }

    public PagedResponse(List<ProductVO> payload) {
        this.payload = payload;
    }

    public PagedResponse(List<ProductVO> payload, Long totalElements) {
        this.payload = payload;
        this.totalElements = totalElements;
    }

    public List<ProductVO> getPayload() {
        return payload;
    }

    public void setPayload(List<ProductVO> payload) {
        this.payload = payload;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
