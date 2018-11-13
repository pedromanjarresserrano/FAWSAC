package com.gitlab.pedrioko.core.view.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The Class LocaleUtil.
 */
public class LocaleUtil {

    /**
     * The locale.
     */
    private static Locale locale;

    /**
     * The aux.
     */
    private static String aux;


    /**
     * Gets the lista paises nombres.
     *
     * @param locale the locale
     * @return the lista paises nombres
     */
    public static List<String> getListaPaisesNombres(Locale locale) {
        String[] locales = Locale.getISOCountries();
        List<String> paises = new ArrayList<>();
        Arrays.asList(locales).forEach(e -> paises.add(new Locale("", e).getDisplayCountry(locale)));
        return paises;
    }

    /**
     * Gets the lista paises nombres.
     *
     * @return the lista paises nombres
     */
    public static List<String> getListaPaisesNombres() {
        String[] locales = Locale.getISOCountries();
        List<String> paises = new ArrayList<>();
        Arrays.asList(locales).forEach(e -> paises.add(new Locale("", e).getDisplayCountry()));
        return paises;
    }

    /**
     * Gets the lista A paises.
     *
     * @return the lista A paises
     */
    public static List<Locale> getListaAPaises() {
        String[] locales = Locale.getISOCountries();
        List<Locale> paises = new ArrayList<>();
        Arrays.asList(locales).forEach(e -> paises.add(new Locale("", e)));
        return paises;
    }

    /**
     * Gets the locale pais.
     *
     * @param displaycontry the displaycontry
     * @return the locale pais
     */
    public static Locale getLocalePais(String displaycontry) {
        String[] locales = Locale.getISOCountries();
        Arrays.asList(locales).forEach(e -> {
            Locale temp = new Locale("", e);
            if (temp.getDisplayCountry().equals(displaycontry)) {
                locale = temp;
            } else {
                temp = null;
            }
        });
        return locale;
    }

    /**
     * Gets the locale pais ISO.
     *
     * @param displaycontry the displaycontry
     * @param locale        the locale
     * @return the locale pais ISO
     */
    public static String getLocalePaisISO(String displaycontry, Locale locale) {
        String[] locales = Locale.getISOCountries();
        aux = "";
        Arrays.asList(locales).forEach(e -> {
            Locale temp = new Locale("", e);
            if (temp.getDisplayCountry(locale).equals(displaycontry)) {
                aux = e;
            }
        });
        return aux;
    }


    /**
     * Gets the locale pais.
     *
     * @param displaycontry the displaycontry
     * @param actualLocale  the actual locale
     * @return the locale pais
     */
    public static Locale getLocalePais(String displaycontry, Locale actualLocale) {
        String[] locales = Locale.getISOCountries();
        Arrays.asList(locales).forEach(e -> {
            Locale temp = new Locale("", e);
            if (temp.getDisplayCountry().equals(displaycontry)) {
                locale = temp;
            } else {
                temp = null;
            }
        });
        return locale;
    }

}
