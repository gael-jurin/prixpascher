package org.nuvola.mobile.prixpascher.dto;

import java.util.Comparator;
import java.util.Date;

public class PriceHistoryVO {
    private Double price;
    private Date priceDate;

    public PriceHistoryVO() {
    }

    public PriceHistoryVO(Double price, Date priceDate) {
        this.price = price;
        this.priceDate = priceDate;
    }

    public static Comparator<PriceHistoryVO> ASC_ORDER = new Comparator<PriceHistoryVO>() {
        public int compare(PriceHistoryVO str1, PriceHistoryVO str2) {
            int res = str1.getPriceDate().compareTo(str2.getPriceDate());
            return res;
        }
    };

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }
}
