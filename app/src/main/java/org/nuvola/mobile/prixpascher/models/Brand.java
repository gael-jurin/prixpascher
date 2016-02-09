package org.nuvola.mobile.prixpascher.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Brand {
    ACCENT(new Category[] {Category.informatique, Category.image_son}, new String[]{"ACCENT"}),
    ACCURIST(new Category[] {Category.accessoires_mode}, new String[]{"ACCURIST"}),
    ACER(new Category[] {Category.informatique, Category.image_son}, new String[]{"ACER"}),
    ADIDAS(new Category[] {Category.sport}, new String[]{"ADIDAS"}),
    APPLE(new Category[] {Category.telephonie, Category.tablette},
            new String[]{"APPLE", "IPHONE", "IPAD", "IMAC", "MACBOOK"}),
    ASUS(new Category[] {Category.informatique, Category.tablette}, new String[]{"ASUS"}),
    AUDI(new Category[] {Category.vehicule}, new String[]{"AUDI"}),
    BEKO(new Category[] {Category.electromenager}, new String[]{"BEKO"}),
    BLACKBERRY(new Category[] {Category.telephonie}, new String[]{"BLACKBERRY"}),
    BMW(new Category[] {Category.vehicule}, new String[]{"BMW"}),
    BOSCH(new Category[] {Category.electromenager}, new String[]{"BOSCH"}),
    CANNE(new Category[] {Category.equipement, Category.poussettes}, new String[]{"CANNE"}),
    CANON(new Category[] {Category.image_son, Category.imprimantes, Category.peripheriques}, new String[]{"CANON"}),
    CHICCO(new Category[] {Category.equipement, Category.poussettes}, new String[]{"CHICCO"}),
    DELL(new Category[] {Category.informatique}, new String[]{"DELL", "INSPIRON", "ULTRABOOK", "LATITUDE", "XPS"}),
    EDIFICE(new Category[] {Category.accessoires_mode}, new String[]{"EDIFICE"}),
    ELECTROLUX(new Category[] {Category.electromenager}, new String[]{"ELECTROLUX"}),
    EPSON(new Category[] {Category.informatique, Category.peripheriques}, new String[]{"EPSON"}),
    FAGOR(new Category[] {Category.electromenager}, new String[]{"FAGOR"}),
    FESTINA(new Category[] {Category.accessoires_mode}, new String[]{"FESTINA"}),
    FIAT(new Category[] {Category.vehicule}, new String[]{"FIAT"}),
    FISHERPRICE(new Category[] {Category.equipement, Category.jouets}, new String[]{"FISHER", "FISHER-PRICE"}),
    FORD(new Category[] {Category.vehicule}, new String[]{"FORD"}),
    GREENWICH(new Category[] {Category.accessoires_mode}, new String[]{"GREENWICH"}),
    GOODBABY(new Category[] {Category.equipement}, new String[]{"GOODBABY"}),
    GUESS(new Category[] {Category.accessoires_mode}, new String[]{"GUESS"}),
    HAIER(new Category[] {Category.electromenager, Category.image_son}, new String[]{"HAIER"}),
    HELLOKITTY(new Category[] {Category.equipement, Category.jouets}, new String[]{"HELLOKITTY", "KITTY"}),
    HM(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"HM"}),
    HP(new Category[] {Category.informatique, Category.peripheriques}, new String[]{"HP", "ELITEBOOK", "HEWLETT-PACKARD"}),
    HTC(new Category[] {Category.telephonie}, new String[]{"HTC", "DESIRE","ONE", "ONE X"}),
    HUAWEI(new Category[] {Category.telephonie}, new String[]{"HUAWEI"}),
    HYUNDAI(new Category[] {Category.vehicule}, new String[]{"HYUNDAI"}),
    IBM(new Category[] {Category.informatique, Category.peripheriques}, new String[]{"IBM", "THINKPAD"}),
    INDESIT(new Category[] {Category.electromenager}, new String[]{"INDESIT"}),
    JBL(new Category[] {Category.image_son},new String[]{"JBL"}),
    KYA(new Category[] {Category.vehicule}, new String[]{"KYA"}),
    CELIO(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"CELIO"}),
    ESPRIT(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"ESPRIT"}),
    GUCCI(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"GUCCI"}),
    LAREDOUTE(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"LAREDOUTE"}),
    LEVIS(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"LEVI'S"}),
    LONGINES(new Category[] {Category.accessoires_mode}, new String[]{"LONGINES"}),
    LG(new Category[] {Category.telephonie, Category.image_son, Category.electromenager},new String[]{"LG", "G3"}),
    LENOVO(new Category[] {Category.informatique, Category.peripheriques}, new String[]{"LENOVO", "YOGA"}),
    LEXMARK(new Category[] {Category.informatique, Category.peripheriques},new String[]{"LEXMARK"}),
    LEXUS(new Category[] {Category.vehicule}, new String[]{"LEXUS"}),
    MAMALOVE(new Category[] {Category.equipement, Category.poussettes}, new String[]{"MAMALOVE"}),
    MERCEDES(new Category[] {Category.vehicule}, new String[]{"MERCEDES"}),
    MOTOROLA(new Category[] {Category.telephonie},new String[]{"MOTOROLA", "RAZOR"}),
    MOULINEX(new Category[] {Category.electromenager}, new String[]{"MOULINEX"}),
    NIKON(new Category[] {Category.image_son}, new String[]{"NIKON"}),
    NIKE(new Category[] {Category.sport}, new String[]{"NIKE"}),
    NOKIA(new Category[] {Category.telephonie}, new String[]{"NOKIA", "LUMIA"}),
    ORIFLAME(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"ORIFLAME"}),
    PANASONIC(new Category[] {Category.image_son, Category.imprimantes},new String[]{"PANASONIC"}),
    PHILIPS(new Category[] {Category.telephonie, Category.image_son},new String[]{"PHILIPS"}),
    RICOH(new Category[] {Category.informatique, Category.peripheriques},new String[]{"RICOH"}),
    SAMSUNG(new Category[] {Category.telephonie, Category.tablette, Category.informatique, Category.electromenager, Category.image_son},new String[]{"SAMSUNG"}),
    SHARP(new Category[] {Category.electromenager, Category.image_son},new String[]{"SHARP", "AQUOS"}),
    SIEMENS(new Category[] {Category.informatique, Category.electromenager, Category.image_son},new String[]{"SIEMENS"}),
    SIERA(new Category[] {Category.electromenager}, new String[]{"SIERA"}),
    SIMPLICITY(new Category[] {Category.equipement, Category.poussettes}, new String[]{"SIMPLICITY"}),
    STORIO(new Category[] {Category.equipement, Category.jouets}, new String[]{"STORIO"}),
    SONY(new Category[] {Category.informatique, Category.image_son, Category.telephonie},new String[]{"SONY", "BRAVIA", "VAIO", "PS3", "PSVITA", "XPERIA"}),
    THOMSON(new Category[] {Category.informatique, Category.peripheriques, Category.electromenager, Category.image_son}, new String[]{"THOMSON"}),
    TOSHIBA(new Category[] {Category.informatique, Category.electromenager, Category.image_son}, new String[]{"TOSHIBA"}),
    TOYOTA(new Category[] {Category.vehicule}, new String[]{"TOYOTA"}),
    WHIRLPOOL(new Category[] {Category.electromenager}, new String[]{"WHIRLPOOL"}),
    XEROX(new Category[] {Category.informatique, Category.peripheriques, Category.imprimantes}, new String[]{"XEROX"}),
    ROLEX(new Category[] {Category.accessoires_mode}, new String[]{"ROLEX"}),
    TISSOT(new Category[] {Category.accessoires_mode}, new String[]{"TISSOT"}),
    ZARA(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"ZARA"}),
    PULLnBEAR(new Category[] {Category.accessoires_mode, Category.fashion}, new String[]{"PULLnBEAR"}),
    REEBOK(new Category[] {Category.sport}, new String[]{"REEBOK"}),
    SEVERIN(new Category[] {Category.electromenager}, new String[]{"SEVERIN"}),
    AUTRES(new Category[] {Category.all}, new String[]{"AUTRES"});

    private String[] subBrands;
    private Category[] categories;

    Brand(Category[] categories, String[] subBrands) {
        this.categories = categories;
        this.subBrands = subBrands;
    }

    public static List<String> brandNames() {
        List<String> labels = new ArrayList<>();
        for (Brand brand: values()) {
            labels.addAll(Arrays.asList(brand.subBrands));
        }
        return labels;
    }

    public static Brand getBrand(String brandName) {
        for (Brand brand: values()) {
            if (Arrays.asList(brand.subBrands).contains(brandName)) {
                return  brand;
            }
        }
        return AUTRES;
    }

    public static List<Brand> getBrandsMatchingCatgeory(Category category) {
        List<Brand> brands = new ArrayList<>();
        if (category.equals(Category.all)) {
            brands.addAll(Arrays.asList(Brand.values()));
        } else {
            brands.add(AUTRES);
            for (Brand brand : values()) {
                if (Arrays.asList(brand.categories).contains(category)) {
                    brands.add(brand);
                }
            }
        }
        return brands;
    }

    public Category[] getCategories() {
        return categories;
    }

    public String[] getSubBrands() {
        return subBrands;
    }
}
