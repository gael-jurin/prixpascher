package org.nuvola.mobile.prixpascher.models;

import java.util.Arrays;

public enum City {
    ___("0;0;0"),
    Casablanca("20000;376;183"),
    Rabat("10000;400;171"),
    Mohammedia("20800;303;177"),
    Agadir("80000;231;378"),
    El_Jadida("24000;338;192"),
    Essaouira("44000;261;255"),
    Meknes("50000;463;187"),
    Fes("30000;485;175"),
    Beni_Mellal("23000;412;273"),
    Marrakech("40000;335;319"),
    Ouarzazate("45000;393;338"),
    Tanger("90000;461;73"),
    Ceuta("51701;540;70"),
    Larache("92000;439;111"),
    Kenitra("14000;416;150"),
    Safi("46000;287;225"),
    Taroudannt("83000;263;383"),
    Erfoud("52200;597;320"),
    Errachidia("52000;340;384"),
    Azrou("53100;505;247"),
    Sefrou("31000;514;222"),
    Taza("35000;555;168"),
    Oujda("60000;703;127"),
    Nador("62000;679;110"),
    Mellila("13200;635;88"),
    Al_Hoceima("32000;579;102"),
    Tetouan("93000;513;90"),
    Chefchaouen("91000;500;112"),
    Ouezzane("16200;501;143"),
    Mhamid("45400;469;399"),
    Tata("84000;302;397"),
    Tiznit("85000;124;422"),
    Tarfaya("70050;47;495"),
    Figuig("61000;754;270"),
    Zagora("45900;457;359");

    private String coordinates;

    City(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public static String[] names() {
        return Arrays.toString(City.values()).replaceAll("_", " ").replaceAll("^.|.$", "").split(", ");
    }
}
