package com.ztiany.module_a.feature;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 18:23
 */
class ModuleAPresenter {

    private final ModuleAView mModuleA_View;

    @Inject
    ModuleAPresenter(ModuleAView moduleA_View) {
        mModuleA_View = moduleA_View;
    }

    void start() {
        mModuleA_View.showMessage("ModuleAPresenter started");
    }

}
