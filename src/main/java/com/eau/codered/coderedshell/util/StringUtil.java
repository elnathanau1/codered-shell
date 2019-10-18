package com.eau.codered.coderedshell.util;

public class StringUtil {

    // https://www.dotnetperls.com/truncate-java
    public static String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }
}
