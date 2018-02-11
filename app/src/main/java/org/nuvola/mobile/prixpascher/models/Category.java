package org.nuvola.mobile.prixpascher.models;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Category {
    all(null, new String[]{}, new String[]{""}),
    telephonie(null, new String[]{}, new String[]{""}),
    android(telephonie, new String[]{"accessoires_phone", "smartwatch"}, new String[]{"chargeur" ,"powerbank", "pochette", "coque", "film", "protection"}),
    iphone(telephonie, new String[]{"accessoires_phone", "smartwatch"}, new String[]{"chargeur" ,"powerbank", "pochette", "coque", "film", "protection"}),
    windows(telephonie, new String[]{"accessoires_phone", "smartwatch"}, new String[]{"chargeur" ,"powerbank", "pochette", "coque", "film", "protection"}),
    blackberry(telephonie, new String[]{"accessoires_phone", "smartwatch"}, new String[]{"chargeur" ,"powerbank", "pochette", "coque", "film", "protection"}),
    accessoires_phone(telephonie, new String[]{}, new String[]{""}),
    smartwatch(telephonie, new String[]{}, new String[]{""}),
    gps(telephonie, new String[]{}, new String[]{""}),
    tablette(null, new String[]{}, new String[]{""}),
    android_tablette(tablette, new String[]{"accessoires_tablette"}, new String[]{"chargeur", "pochette", "coque", "film", "protection"}),
    ipad(tablette, new String[]{"accessoires_tablette"}, new String[]{"chargeur", "pochette", "coque", "film", "protection"}),
    windows8(tablette, new String[]{"accessoires_tablette"}, new String[]{"chargeur", "pochette", "coque", "film", "protection"}),
    accessoires_tablette(tablette, new String[]{}, new String[]{""}),
    informatique(null, new String[]{}, new String[]{""}),
    ordinateur_fixe(informatique, new String[]{"peripheriques", "imprimantes"}, new String[]{"onduleur", "clavier"}),
    portable(informatique, new String[]{"peripheriques"}, new String[]{"sacoche", "sac", "maintenance", "cartable", "souris", "clavier", "adaptateur", "cle", "usb", "disque", "graveur"}),
    imprimantes(informatique, new String[]{}, new String[]{""}),
    peripheriques(informatique, new String[]{}, new String[]{""}),
    serveurs(informatique, new String[]{"peripheriques", "imprimantes"}, new String[]{"onduleur", "clavier"}),
    image_son(null, new String[]{}, new String[]{""}),
    tv(image_son, new String[]{}, new String[]{"recepteurs", "home_cinema", "support", "pinacle", "projecteur", "recepteur"}),
    home_cinema(image_son, new String[]{"tv", "jeux_videos"}, new String[]{"led", "lcd", "console"}),
    lecteurs(image_son, new String[]{"tv", "jeux_videos"}, new String[]{"led", "lcd", "console"}),
    jeux_videos(image_son, new String[]{}, new String[]{"abonnement", "jeu", "mannette", "gamepad"}),
    recepteurs(image_son, new String[]{"tv", "recepteurs"}, new String[]{"led", "lcd", "antenne", "satellite"}),
    photo(image_son, new String[]{"photo"}, new String[]{"sd"}),
    lunettes_3d(image_son, new String[]{""}, new String[]{""}),
    electromenager(null, new String[]{}, new String[]{""}),
    refrigerateur(electromenager, new String[]{"lave_linge", "lavage_sechage", "cuisine"}, new String[]{"machine", "micro", "robot", "four"}),
    lave_linge(electromenager, new String[]{"refrigerateur", "lavage_sechage", "cuisine"}, new String[]{"micro","four","robot","refrigerateur"}),
    lavage_sechage(electromenager, new String[]{"lave_linge", "refrigerateur", "cuisine"}, new String[]{"micro","four","robot","refrigerateur"}),
    cuisine(electromenager, new String[]{"lave_linge", "refrigerateur", "lavage_sechage"}, new String[]{""}),
    lave_vaisselle(electromenager, new String[]{"lave_linge", "refrigerateur", "lavage_sechage"}, new String[]{""}),
    climatiseur(electromenager, new String[]{"lave_linge", "refrigerateur", "lavage_sechage"}, new String[]{""}),
    aspirateur(electromenager, new String[]{"lave_linge", "refrigerateur", "climatiseur", "lavage_sechage"}, new String[]{""}),
    fashion(null, new String[]{}, new String[]{""}),
    bebes(fashion, new String[]{"poussettes", "jouets"}, new String[]{"malibu", "youpala"}),
    filles(fashion, new String[]{"femme_chaussures"}, new String[]{""}),
    garcons(fashion, new String[]{"homme_chaussures"}, new String[]{""}),
    femmes(fashion, new String[]{"femme_chaussures"}, new String[]{"escarpin", "sandale", "bottine"}),
    hommes(fashion, new String[]{"homme_chaussures"}, new String[]{"botte", "richelieu", "mocassin"}),
    soins_cosmetiques(fashion, new String[]{""}, new String[]{""}),
    accessoires_fashion(fashion, new String[]{""}, new String[]{""}),
    sport(null, new String[]{}, new String[]{""}),
    men_baskets(sport, new String[]{"men_survets"}, new String[]{""}),
    ladies_baskets(sport, new String[]{"ladies_survets"}, new String[]{""}),
    men_survets(sport, new String[]{"men_baskets"}, new String[]{""}),
    ladies_survets(sport, new String[]{"ladies_baskets"}, new String[]{""}),
    fitness(sport, new String[]{}, new String[]{""}),
    homme_chaussures(fashion, new String[]{}, new String[]{"veste", "pantalon", "costume", "chemise", "jacket", "blouson"}),
    femme_chaussures(fashion, new String[]{}, new String[]{"veste", "pantatlon", "chemise", "jacket", "robe", "blouson"}),
    accessoires_mode(null, new String[]{}, new String[]{""}),
    homme_montres(accessoires_mode, new String[]{"homme_lunettes"}, new String[]{""}),
    femme_montres(accessoires_mode, new String[]{"femme_lunettes", "sacs", "soins_cosmetiques"}, new String[]{""}),
    homme_lunettes(accessoires_mode, new String[]{"homme_montres"}, new String[]{""}),
    femme_lunettes(accessoires_mode, new String[]{"femme_montres", "sacs", "soins_cosmetiques"}, new String[]{""}),
    sacs(accessoires_mode, new String[]{"femme_montres", "femme_lunettes", "soins_cosmetiques"}, new String[]{"bracelet", "collier", "pendentif", "bague"}),
    homme_parfums(accessoires_mode, new String[]{"homme_montres", "homme_lunettes"}, new String[]{""}),
    femme_parfums(accessoires_mode, new String[]{"femme_montres", "femme_lunettes"}, new String[]{""}),
    bijoux(accessoires_mode, new String[]{"femme_montres", "sacs", "femme_lunettes"}, new String[]{""}),
    equipement(null, new String[]{}, new String[]{""}),
    jouets(equipement, new String[]{"poussettes"}, new String[]{"maxi", "youpala"}),
    poussettes(equipement, new String[]{"jouets"}, new String[]{"vtech", "playmobil"}),
    maison(equipement, new String[]{"maison"}, new String[]{"lit", "matelas", "salon", "tapis"}),
    peche(equipement, new String[]{"peche"}, new String[]{"leurre", "moulinet"}),
    vehicule(null, new String[]{}, new String[]{""}),
    immobilier(null, new String[]{}, new String[]{""});

    private Category parent;
    private String[] linkedWords;
    private String[] linkedCategories;

    Category(Category parent, String[] linkedCategories, String[] linkedWords) {
        this.parent = parent;
        this.linkedCategories = linkedCategories;
        this.linkedWords = linkedWords;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String[] getLinkedCategories() {
        return linkedCategories;
    }

    public String[] getLinkedWords() {
        return linkedWords;
    }

    public static Map<Category, List<Category>> getCategories() {
        Map<Category, List<Category>> categories = new HashMap<>();

        categories.put(telephonie, Arrays.asList(android, iphone, blackberry, windows, accessoires_phone));
        categories.put(tablette, Arrays.asList(android_tablette, ipad, windows8, accessoires_tablette));
        categories.put(informatique, Arrays.asList(imprimantes, ordinateur_fixe, portable, serveurs, peripheriques));
        categories.put(image_son, Arrays.asList(tv, home_cinema, jeux_videos, photo, recepteurs));
        categories.put(electromenager, Arrays.asList(refrigerateur, lave_linge, lavage_sechage, climatiseur,
                aspirateur, cuisine, lave_vaisselle));
        categories.put(fashion, Arrays.asList(hommes, femmes, bebes, filles, garcons, homme_chaussures, femme_chaussures));
        categories.put(accessoires_mode, Arrays.asList(homme_montres, femme_montres, femme_lunettes,
                homme_lunettes, homme_parfums, femme_parfums, sacs, bijoux));
        categories.put(sport, Arrays.asList(men_baskets, ladies_baskets, men_survets, ladies_survets, fitness));
        categories.put(equipement, Arrays.asList(poussettes, jouets));

        return categories;
    }

    public static List<Category> getAnnounceCategories() {
        List<Category> categories = new ArrayList<>();

        categories.add(telephonie);
        categories.add(tablette);
        categories.add(informatique);
        categories.add(image_son);
        categories.add(electromenager);
        categories.add(fashion);
        categories.add(accessoires_mode);
        categories.add(sport);
        categories.add(equipement);
        categories.add(vehicule);
        categories.add(immobilier);

        return categories;
    }

    public static String[] names() {
        return Arrays.toString(Category.values()).replaceAll("_", " ").replaceAll("^.|.$", "").split(", ");
    }

    public static List<Category> filterValues() {
        List<Category> filterValues = new ArrayList<>();
        for (Category category: values()) {
            if (!category.equals(all)) {
                filterValues.add(category);
            }
        }

        Collections.sort(filterValues, ALPHABETICAL_ORDER);
        return filterValues;
    }

    public static List<Category> publishedCategoryValues() {
        List<Category> filterValues = new ArrayList();
        filterValues.add(Category.refrigerateur);
        filterValues.add(Category.recepteurs);
        filterValues.add(Category.climatiseur);
        filterValues.add(Category.lave_linge);
        return filterValues;
    }

    public static List<Category> offerCategoryValues() {
        List<Category> filterValues = new ArrayList();
        filterValues.add(Category.electromenager);
        filterValues.add(Category.image_son);
        filterValues.add(Category.informatique);
        filterValues.add(Category.telephonie);
        filterValues.add(Category.tablette);
        filterValues.add(Category.equipement);
        return filterValues;
    }

    public static List<Category> filterSubCategoryValues() {
        List<Category> filterValues = new ArrayList();
        for (Category category: values()) {
            if (category.getParent() != null) {
                filterValues.add(category);
            }
        }
        Collections.sort(filterValues, ALPHABETICAL_ORDER);
        return filterValues;
    }

    public static String[] filterMobCategoryValues() {
        List<String> filterValues = new ArrayList();
        for (Category category: indexableCategories()) {
            filterValues.add(category.name().replaceAll("_", " "));
        }
        String[] array = new String[filterValues.size()];
        filterValues.toArray(array);
        return array;
    }

    public static List<String> detailCategories() {
        List<String> detailCategories = new ArrayList();
        detailCategories.add(Category.telephonie.name());
        detailCategories.add(Category.tablette.name());
        detailCategories.add(Category.image_son.name());
        detailCategories.add(Category.informatique.name());
        return detailCategories;
    }

    public static List<Category> subCategories(Category parent) {
        if (parent.equals(all)) {
            return Arrays.asList(Category.values());
        } else {
            List<Category> subCategories = new ArrayList<>();
            parent = parent.getParent() == null ? parent : parent.getParent();
            for (Category category: values()) {
                if (!category.equals(all) && parent.equals(category.getParent())) {
                    subCategories.add(category);
                }
            }
            return subCategories;
        }
    }

    public static List<Category> indexableCategories() {
        List<Category> indexableCategories =  new ArrayList<>();
        for (Category category: values()) {
            if (!category.equals(all) && category.getParent() == null) {
                indexableCategories.add(category);
            }
        }
        Collections.sort(indexableCategories, ALPHABETICAL_ORDER);
        return indexableCategories;
    }

    public static Category matchingValueOf(String name) {
        if (name != null && !name.isEmpty()) {
            return Category.all;
        }
        for (Category category: values()) {
            if (category.name().contains(name)) {
                return category;
            }
        }
        return valueOf(name);
    }

    public static List<DrawerMenuItem> mobileAppCategories(Context context) {
        List<DrawerMenuItem> iconableCategories =  new ArrayList<>();
        for (Category category: getCategories().keySet()) {
            if (!category.equals(all) && category.getParent() == null) {
                String label = getLabel(category);
                DrawerMenuItem item = new DrawerMenuItem(category.name(),
                        getDrawable(context, "pub_" + category.name()));
                item.setAvt(label);
                iconableCategories.add(item);
            }
        }
        return iconableCategories;
    }

    @NonNull
    public static String getLabel(Category category) {
        String label = category.name();
        if (category.equals(equipement)) {
            label = "Maison & Loisirs";
        } else {
            label = label.replaceAll("_", " ");
        }
        return label.toUpperCase();
    }

    private static Comparator<Category> ALPHABETICAL_ORDER = new Comparator<Category>() {
        public int compare(Category str1, Category str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1.name(), str2.name());
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };

    private static int getDrawable(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }
}
