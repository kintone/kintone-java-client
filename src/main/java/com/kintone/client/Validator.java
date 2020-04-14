package com.kintone.client;

import java.util.regex.Pattern;

class Validator {
    private static final Pattern NOT_CONTROL_CHARS_PATTERN = Pattern.compile("[^\\p{Cntrl}]+");

    static void checkContentType(String contentType) {
        boolean valid = NOT_CONTROL_CHARS_PATTERN.matcher(contentType).matches();
        if (!valid) {
            throw new IllegalArgumentException("Invalid contentType: " + contentType);
        }
    }

    static void checkFilename(String filename) {
        boolean valid = NOT_CONTROL_CHARS_PATTERN.matcher(filename).matches();
        if (!valid) {
            throw new IllegalArgumentException("Invalid filename: " + filename);
        }
    }
}
