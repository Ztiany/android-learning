package com.dongnao.router.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author Lance
 * @date 2018/2/22
 */

public class Utils {


    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

}
