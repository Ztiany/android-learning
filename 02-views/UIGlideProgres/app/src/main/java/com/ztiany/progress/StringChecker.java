package com.ztiany.progress;

public class StringChecker {

    private StringChecker() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}
