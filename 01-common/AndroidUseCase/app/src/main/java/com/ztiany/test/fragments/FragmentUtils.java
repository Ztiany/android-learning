package com.ztiany.test.fragments;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 *   Fragment相关操作
 *
 * @author Ztiany
 *         mail: ztiany3@gmail.com
 * @version 1.0
 */
public class FragmentUtils {

    /**
     * 默认的Fragment容器id，在类似{@link #replace(FragmentManager, Fragment)}没有指定id的方法中，默认使用此id
     */
    private static int sContainerId;

    private FragmentUtils() {
        throw new UnsupportedOperationException("FragmentUtils cannot be instantiation");
    }

    /**
     * 设置一个全局的默认容器id，当没有指定id时，则使用此id操作fragment
     *
     * @param containerId 容器id
     */
    public static void init(@IdRes int containerId) {
        sContainerId = containerId;
    }


    @SuppressWarnings("unchecked")
    public static <F extends Fragment> F findFragmentByTag(FragmentManager fragmentManager, String tag) {
        return (F) fragmentManager.findFragmentByTag(tag);
    }


    public static void replace(@IdRes int layoutId, FragmentManager fragmentManager, Fragment fragment, boolean addToStack, boolean transit) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(layoutId, fragment, fragment.getClass().getName());
        if (transit) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        if (addToStack) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        ft.commit();
    }

    public static void replace(@IdRes int layoutId, FragmentManager fragmentManager, Fragment fragment, boolean addToStack) {
        replace(layoutId, fragmentManager, fragment, addToStack, false);
    }

    public static void replace(@IdRes int layoutId, FragmentManager fragmentManager, Fragment fragment) {
        replace(layoutId, fragmentManager, fragment, false, false);
    }

    public static void replace(FragmentManager fragmentManager, Fragment fragment) {
        replace(fragmentManager, fragment, false, false);
    }

    public static void replace(FragmentManager fragmentManager, Fragment fragment, boolean addToStack) {
        replace(fragmentManager, fragment, addToStack, false);
    }

    public static void replace(FragmentManager fragmentManager, Fragment fragment, boolean addToStack, boolean transit) {
        if (sContainerId == 0) {
            throw new IllegalStateException("FragmentUtils need initialize containerId");
        }
        replace(sContainerId, fragmentManager, fragment, addToStack, transit);
    }

    public static void popBackToTop(FragmentManager fragmentManager, boolean immediate) {
        if (immediate) {
            fragmentManager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            fragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void popBackToTop(FragmentManager fragmentManager) {
        popBackToTop(fragmentManager, false);
    }

    public static <T> T requireContextImplement(Fragment fragment, Class<T> clazz) {
        if (!clazz.isInstance(fragment.getActivity())) {
            throw new RuntimeException("use newFragment:" + fragment + ", Activity must impl Class :" + clazz);
        } else {
            return clazz.cast(fragment.getActivity());
        }
    }

    public static void fragmentBack(Fragment fragment, boolean immediate) {

        FragmentManager supportFragmentManager = fragment.getActivity().getSupportFragmentManager();
        int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();

        if (backStackEntryCount > 0) {

            if (immediate) {
                supportFragmentManager.popBackStackImmediate();
            } else {
                supportFragmentManager.popBackStack();
            }

        } else {
            fragment.getActivity().supportFinishAfterTransition();
        }
    }

}
