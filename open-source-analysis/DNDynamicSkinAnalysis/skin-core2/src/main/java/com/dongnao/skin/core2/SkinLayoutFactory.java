package com.dongnao.skin.core2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.dongnao.skin.core2.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2018/3/16 0016.
 */
public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {

    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    private static final Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();

    private Activity activity;

    // 属性处理类
    SkinAttribute skinAttribute;

    public SkinLayoutFactory(Activity activity, Typeface typeface) {
        this.activity = activity;
        skinAttribute = new SkinAttribute(typeface);
    }

    private static final String TAG = "SkinLayoutFactory";

    private int count = 0;

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //反射 classloader
        View view = createViewFromTag(name, context, attrs);
        // 自定义View
        if (null == view) {
            view = createView(name, context, attrs);
        }
        //筛选符合属性的View
        skinAttribute.load(view, attrs);
        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        //包含了 . 自定义控件
        if (-1 != name.indexOf(".")) {
            return null;
        }
        View view = null;
        for (int i = 0; i < mClassPrefixList.length; i++) {
            view = createView(mClassPrefixList[i] + name, context, attrs);
            if (null != view) {
                break;
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = aClass.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
            }
        }
        if (null != constructor) {
            try {
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBar(activity);
        Typeface typeface = SkinThemeUtils.getSkinTypeface(activity);
        // 更换皮肤
        skinAttribute.applySkin(typeface);
    }

}
