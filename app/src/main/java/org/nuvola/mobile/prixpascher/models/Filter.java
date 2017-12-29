package org.nuvola.mobile.prixpascher.models;

public abstract class Filter {
    private Integer page;
    private Integer size;
    private SortField defaultSort;
    private Boolean defaultOrder;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public SortField getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(SortField defaultSort) {
        this.defaultSort = defaultSort;
    }

    public Boolean getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Boolean defaultOrder) {
        this.defaultOrder = defaultOrder;
    }
}
