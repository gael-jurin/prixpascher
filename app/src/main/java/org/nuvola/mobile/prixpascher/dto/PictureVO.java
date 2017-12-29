package org.nuvola.mobile.prixpascher.dto;

public class PictureVO {
    public static String ICON_WIDTH = "150px";
    public static String ICON_WIDTH_MAIN = "350px";
    public static String ICON_HEIGHT = "150px";
    public static String ICON_HEIGHT_MAIN = "350px";

    private String name;
    private String url;
    private String type;
    private Integer position;
    private String height;
    private String width;

    public PictureVO(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public PictureVO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
