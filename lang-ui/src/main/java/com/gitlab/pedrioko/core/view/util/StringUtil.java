package com.gitlab.pedrioko.core.view.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * The Class StringUtil.
 */
public class StringUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    /**
     * The Constant SALTCHARS.
     */
    private static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static Random r;

    static {
        try {
            r = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error on getInstanceStrong()", e);
        }
    }

    /**
     * Gets the salt string.
     *
     * @return the salt string
     */
    public static String getSaltString() {
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 18) {
            int index = r.nextInt(SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    /**
     * Gets the random hex color.
     *
     * @return the random hex color
     */
    public static String getRandomHexColor() {
        String redhex = Long.toHexString(r.nextLong() + r.nextLong() * r.nextLong() / r.nextLong());
        String greenhex = Long.toHexString(r.nextLong() + r.nextLong() + r.nextLong() / r.nextInt());
        String bluehex = Long.toHexString((r.nextLong() + r.nextLong() * r.nextLong()) - r.nextLong());
        String red = redhex.substring(0, 2);
        String green = greenhex.substring(0, 2);
        String blue = bluehex.substring(0, 2);
        return "#" + red + green + blue;
    }

    public static String getCapitalize(String string) {
        return StringUtils.capitalize(string);
    }

    public static String getDescapitalize(String string) {
        return StringUtils.uncapitalize(string);
    }

    public static String lastSplit(String string, String splitter) {
        return string.substring(string.lastIndexOf(splitter) + 1);
    }

    public static String formatFileSize(Object size) {
        return formatFileSize(size, 2);
    }

    public static String formatFileSize(Object size, Integer digits) {
        double bytes = Double.valueOf(size.toString());
        String[] dictionary = {"bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int index = 0;
        for (index = 0; index < dictionary.length; index++) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
        }
        return String.format("%." + digits + "f", bytes) + " " + dictionary[index];
    }
}
