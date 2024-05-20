package com.ztiany.view.material;

import com.ztiany.view.BaseMainActivity;
import com.ztiany.view.Item;
import java.util.List;

public class MaterialComponentActivity extends BaseMainActivity {

    @Override
    protected void provideItems(List<Item> items) {
        items.add(new Item("ShapeableImageView", ShapeableImageViewFragment.class));
        items.add(new Item("MaterialButton", MaterialButtonFragment.class));
        items.add(new Item("CustomShapeLayout", MaterialShapeDrawableFragment.class));
    }

}
