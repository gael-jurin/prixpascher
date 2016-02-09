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
    BIOUGNACH(new String[]{"LG", "RP"}),
    PINKANDBLUE(new String[]{"LG", "RP"}),
    MAGANAT(new String[]{"LG", "RP"}),
    MICROCHOIX(new String[]{"LG", "SR", "SAV", "PC"}),
    PLANETTVSAT(new String[]{"LG", "SR", "SAV", "PC"}),
    BOUTIKA(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    LAROSE(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
    LAREDOUTE(new String[]{"LG", "SR", "PL", "SAV", "PC"}),
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
    TONPC(new String[]{"LG", "RP"}),
    MICROSTORE(new String[]{"LG", "RP"}),
    CHOIXMA(new String[]{""}),
    MICROLAND(new String[]{"LG", "PL"}),
    SPORTPLUS(new String[]{"LG", "PL"}),
    UNIDEALS(new String[]{"LG", "SR", "PL", "PC"});

    private String[] features;

    ShopType(String[] features) {
        this.features = features;
    }

    public static List<ShopType> affilliates() {
        List<ShopType> shopTypeList = new ArrayList<>();
        shopTypeList.add(ALAMODE);
        shopTypeList.add(ASWAKASALAM);
        shopTypeList.add(BOUTIKA);
        shopTypeList.add(CHOIXMA);
        shopTypeList.add(HMALL);
        shopTypeList.add(JUMIA);
        shopTypeList.add(LESJOUETSMA);
        shopTypeList.add(LAROSE);
        shopTypeList.add(LAREDOUTE);
        shopTypeList.add(MAGANAT);
        shopTypeList.add(MAROCVENTES);
        shopTypeList.add(MICROCHOIX);
        shopTypeList.add(MICROLAND);
        shopTypeList.add(MICROSTORE);
        shopTypeList.add(MPRICE);
        shopTypeList.add(ORIFLAME);
        shopTypeList.add(PLANETTVSAT);
        shopTypeList.add(PULLnBEAR);
        shopTypeList.add(PINKANDBLUE);
        shopTypeList.add(SHOPPEOS);
        shopTypeList.add(TANGEROIS);
        shopTypeList.add(TONPC);
        shopTypeList.add(VENTEONLINE);
        shopTypeList.add(YOUPICOMA);
        shopTypeList.add(COSMOS);
        shopTypeList.add(GEMEAUXSAT);
        shopTypeList.add(UNIVERSPROMO);
        shopTypeList.add(UNIDEALS);
        shopTypeList.add(ZARA);
        return shopTypeList;
    }

    public static List<String> detailShops() {
        List<String> detailShops = new ArrayList<>();
        detailShops.add(JUMIA.name());
        detailShops.add(HMALL.name());
        detailShops.add(SHOPPEOS.name());
        detailShops.add(BOUTIKA.name());
        detailShops.add(MAROCVENTES.name());
        return detailShops;
    }

    public static List<String> checkBySignatureShops() {
        List<String> shops = new ArrayList<>();
        shops.add(ZARA.name());
        shops.add(ALAMODE.name());
        shops.add(BIOUGNACH.name());
        shops.add(TANGEROIS.name());
        shops.add(MAROCVENTES.name());
        shops.add(LAREDOUTE.name());
        shops.add(SPORTPLUS.name());
        return shops;
    }

    public static List<String> lowerNames() {
        List<String> shops = new ArrayList<>();
        for (ShopType shopType: values()) {
            shops.add(shopType.name().toLowerCase());
        }
        return shops;
    }

    public static String[] names() {
        return Arrays.toString(ShopType.values()).replaceAll("^.|.$", "").split(", ");
    }
}
