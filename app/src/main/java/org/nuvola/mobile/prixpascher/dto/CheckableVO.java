package org.nuvola.mobile.prixpascher.dto;

import java.util.Comparator;

public class CheckableVO<T> {
    private T data;
    private String label;
    private Boolean checked;

    public CheckableVO() {
    }

    public CheckableVO(T data,
                       String label) {
        this.checked = false;
        this.label = label;
        this.data = data;
    }

    public CheckableVO(T data,
                       String label,
                       Boolean checked) {
        this.checked = checked;
        this.label = label;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public static Comparator<CheckableVO> CHECKED_ORDER = new Comparator<CheckableVO>() {
        public int compare(CheckableVO obj1, CheckableVO obj2) {
            return -Boolean.compare(obj1.getChecked(), obj2.getChecked());
        }
    };
}
