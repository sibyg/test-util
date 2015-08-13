package com.siby.automation.model;

public enum Country {
    UK,
    IRELAND;

    public static Country getCountry(String countryName) {
        if (countryName.equalsIgnoreCase("UK")) return Country.UK;
        else if (countryName.equalsIgnoreCase("Ireland")) return Country.IRELAND;
        return null;
    }
}
