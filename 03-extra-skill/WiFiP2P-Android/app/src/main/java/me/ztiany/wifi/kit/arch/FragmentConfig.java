package me.ztiany.wifi.kit.arch;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 */
public class FragmentConfig {

    private static final int INVALIDATE_ID = -1;
    private static int sDefaultContainerId = INVALIDATE_ID;
    private static FragmentTransitions sFragmentTransitions = new HorizontalTransitions();

    public static void setDefaultContainerId(int defaultContainerId) {
        sDefaultContainerId = defaultContainerId;
    }

    public static int defaultContainerId() {
        if (sDefaultContainerId == INVALIDATE_ID) {
            throw new IllegalStateException("sDefaultContainerId has not set");
        }
        return sDefaultContainerId;
    }

    public static void setDefaultFragmentTransitions(@NonNull FragmentTransitions fragmentTransitions) {
        //noinspection ConstantConditions
        if (fragmentTransitions == null) {
            throw new NullPointerException("fragmentTransitions can not be null.");
        }
        sFragmentTransitions = fragmentTransitions;
    }

    @NonNull
    public static FragmentTransitions defaultFragmentTransitions() {
        return sFragmentTransitions;
    }

}