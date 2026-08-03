package com.reno.common.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostalCodeValidatorTest {
    @Test void acceptsValidIndiaPinCodes() {
        assertTrue(PostalCodeValidator.isValid("IN", "425001"));
        assertTrue(PostalCodeValidator.isValid("India", "411001"));
    }
    @Test void rejectsInvalidIndiaPinCodes() {
        assertFalse(PostalCodeValidator.isValid("IN", "0425001"));
        assertFalse(PostalCodeValidator.isValid("IN", "42500"));
        assertFalse(PostalCodeValidator.isValid("IN", "42500A"));
    }
    @Test void acceptsValidCanadaPostalCodes() {
        assertTrue(PostalCodeValidator.isValid("CA", "M5V 3A8"));
        assertTrue(PostalCodeValidator.isValid("Canada", "K1A 0B1"));
    }
    @Test void rejectsInvalidOrUnsupportedPostalCodes() {
        assertFalse(PostalCodeValidator.isValid("CA", "123456"));
        assertFalse(PostalCodeValidator.isValid("US", "10001"));
    }
}
