package com.reno.common.validation;

import java.util.Locale;
import java.util.regex.Pattern;

public final class PostalCodeValidator {
    private static final Pattern INDIA = Pattern.compile("^[1-9][0-9]{5}$");
    private static final Pattern CANADA = Pattern.compile("^[ABCEGHJ-NPRSTVXY][0-9][ABCEGHJ-NPRSTV-Z][ -]?[0-9][ABCEGHJ-NPRSTV-Z][0-9]$", Pattern.CASE_INSENSITIVE);

    private PostalCodeValidator() {}

    public static boolean isValid(String country, String postalCode) {
        if (country == null || postalCode == null) return false;
        String value = postalCode.trim();
        return switch (country.toUpperCase(Locale.ROOT)) {
            case "IN", "INDIA" -> INDIA.matcher(value).matches();
            case "CA", "CANADA" -> CANADA.matcher(value).matches();
            default -> false;
        };
    }
}
