package org.nuvola.mobile.prixpascher.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ShopType {
    HMALL(new String[]{"LG", "SR", "PL", "PC"}),
    JUMIA(new String[]{"LG", "RG", "SR", "SC","PL", "AM", "PC"}),
    SHOPPEOS(new String[]{"LG", "RG", "SR", "SC", "PL", "PC"}),
    ATTIJARA(new String[]{"LG", "RG", "SR", "PL", "PC"}),
    TANGEROIS(new String[]{"LG", "SR", "GAR", "SAV", "PC"}),
    CHIKOU(new String[]{"LG", "SR", "GAR", "SAV", "PC"}),
    BIOUGNACH(new String[]{"LG", "RP"}),
    PINKANDBLUE(new String[]{"LG", "RP"}),
    MAGANAT(new String[]{"LG", "RP"}),
    MICROCHOIX(new String[]{"LG", "SR", "SAV", "PC"}),
    SESA(new String[]{"LG", "SR", "SAV", "PC"}),
    COSMISHOP(new String[]{"LG", "SR", "SAV", "PC"}),
    PLANETTVSAT(new String[]{"LG", "SR", "SAV", "PC"}),
    BOUTIKA(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    KITEA(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    LAROSE(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    LAREDOUTE(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    PECHEURMA(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    MPRICE(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    COSMOS(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    UNIVERSPROMO(new String[]{"LG", "SR", "PL", "SAV"}),
    GEMEAUXSAT(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    VENTEONLINE(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    LESJOUETSMA(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    YOUPICOMA(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    ZARA(new String[]{""}),
    ORIFLAME(new String[]{""}),
    PULLnBEAR(new String[]{""}),
    AuDERBY(new String[]{""}),
    ALAMODE(new String[]{""}),
    MAROCVENTES(new String[]{""}),
    ASWAKASALAM(new String[]{""}),
    HM(new String[]{""}),
    HOTELIA(new String[]{""}),
    TONPC(new String[]{"LG", "RP"}),
    MICROSTORE(new String[]{"LG", "RP"}),
    CHOIXMA(new String[]{""}),
    MICROLAND(new String[]{"LG", "PL"}),
    SPORTPLUS(new String[]{"LG", "PL"}),
    UNIDEALS(new String[]{"LG", "SR", "PL", "PC"}),
    PCFACTORY(new String[]{"LG", "RP"}),
    BEAUTYSUCCESS(new String[]{"LG", "RP"}),
    BESTMARK(new String[]{"LG", "RP"}),
    MONJOUET(new String[]{"LG", "RP"}),
    LINKSOLUTIONS(new String[]{"LG", "SR", "PL", "PC"}),
    LECOMPTOIRELECTRO(new String[]{"LG", "SR", "PL", "PC"}),
    TABTEL(new String[]{"LG", "SR", "PL", "PC"}),
    NIKE(new String[]{"LG", "SR", "PL", "PC"}),
    ELECBOUSFIHA(new String[]{"LG", "SR", "PL", "PC"}),
    VETEMENTMA(new String[]{"LG", "SR", "PL", "PC"}),
    VIRGIN(new String[]{"LG", "SR", "PL", "PC"}),
    GENERIC(new String[]{"LG", "SR", "PL", "PC"});

    private String[] features;

    ShopType(String[] features) {
        this.features = features;
    }

    public static List<ShopType> affilliates() {
        List<ShopType> shopTypeList = new ArrayList();
        shopTypeList.add(BEAUTYSUCCESS);
        shopTypeList.add(JUMIA);
        shopTypeList.add(LAREDOUTE);
        shopTypeList.add(NIKE);
        return shopTypeList;
    }

    public static List<String> detailShops() {
        List<String> detailShops = new ArrayList();
        detailShops.add(JUMIA.name());
        detailShops.add(NIKE.name());
        detailShops.add(ELECBOUSFIHA.name());
        detailShops.add(LAREDOUTE.name());
        return detailShops;
    }

    public static List<String> lowerNames() {
        List<String> shops = new ArrayList();
        for (ShopType shopType: values()) {
            shops.add(shopType.name().toLowerCase());
        }
        return shops;
    }

    public static String[] names() {
        return Arrays.toString(ShopType.values()).replaceAll("^.|.$", "").split(", ");
    }
}
