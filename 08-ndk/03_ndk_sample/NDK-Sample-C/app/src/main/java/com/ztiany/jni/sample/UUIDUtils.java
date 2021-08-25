package com.ztiany.jni.sample;

import java.util.UUID;

public class UUIDUtils {
    public static String get() {
        return UUID.randomUUID().toString();
    }
}
