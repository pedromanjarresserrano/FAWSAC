package com.gitlab.pedrioko.core.view.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidator {

    /**
     * The Constant EMAILPATTERN.
     */
    private static final String EMAILPATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * The pattern.
     */
    private static Pattern pattern = Pattern.compile(EMAILPATTERN);

    /**
     * Validate email.
     *
     * @param email the email
     * @return true, if successful
     */
    public static boolean validateEmail(String email) {
        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }

    /**
     * Validate if blank.
     *
     * @param strings the strings
     * @return true, if successful
     */
    public static boolean validateIfBlank(String... strings) {
        if (strings == null)
            return false;
        for (String e : strings)
            if (StringUtils.hasLength(e))
                return false;
            else
                return true;
        return false;
    }

}