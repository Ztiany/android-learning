package com.ztiany.main.main;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-17 17:38
 */
@Module
abstract class MainModule {

    @Binds
    abstract MainView mainView(MainActivity act);

    @Binds
    @IntoMap
    @StringKey("A")
    abstract MapValue createAMapValue(MapValue mapValue);

    @Binds
    @IntoMap
    @StringKey("B")
    abstract MapValue createBMapValue(MapValue mapValue);

}

