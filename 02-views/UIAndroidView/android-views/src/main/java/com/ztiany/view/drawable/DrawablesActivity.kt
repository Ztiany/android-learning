package com.ztiany.view.drawable

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import com.ztiany.view.BaseMainActivity
import com.ztiany.view.Item

/**
 * 注意，从源码 [DrawableInflater](https://android.googlesource.com/platform/frameworks/base/+/caca720/graphics/java/android/graphics/drawable/DrawableInflater.java) 可以发现，
 * [ShapeDrawable] 不对应任何 xml 定义，xml 定义的 ShapeDrawable 由 [GradientDrawable] 实现，事实上是  [ShapeDrawable]  拥有的功能 [GradientDrawable] 都有。
 */
class DrawablesActivity : BaseMainActivity() {

    override fun provideItems(items: MutableList<Item>) {
        items.add(Item("Drawable by Code", CodingDrawableFragment::class.java))
        items.add(Item("BitmapDrawable", DrawableBitmapFragment::class.java))
        items.add(Item("LayerDrawable", DrawableLayerFragment::class.java))
        items.add(Item("RotateDrawable", DrawableRotateFragment::class.java))
        items.add(Item("SelectorDrawable", DrawableSelectorFragment::class.java))
        items.add(Item("RippleDrawable", RippleDrawableFragment::class.java))
        items.add(Item("VectorDrawable", DrawableVectorFragment::class.java))
        items.add(Item("FishDrawable", FishDrawableFragment::class.java))
        items.add(Item("SVG China Map", SVGChinaFragment::class.java))
    }

}