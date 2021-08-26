package com.ztiany.main;

import android.app.Activity;
import android.view.View;

import com.ztiany.base.di.ActivityScope;
import com.ztiany.base.di.ViewKey;
import com.ztiany.main.binding.BindingActivity;
import com.ztiany.main.binding.BindingComponent;
import com.ztiany.main.main.CustomTextView;
import com.ztiany.main.main.CustomTextViewComponent;
import com.ztiany.main.main.MainActivity;
import com.ztiany.main.main.MainActivityComponent;
import com.ztiany.main.splash.SplashActivity;
import com.ztiany.main.splash.SplashModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
        MainActivityComponent.class,
        BindingComponent.class,
        CustomTextViewComponent.class
})
public abstract class MainBuildersModule {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    public abstract AndroidInjector.Factory<? extends Activity> bindMainActivityFactory(MainActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(BindingActivity.class)
    public abstract AndroidInjector.Factory<? extends Activity> bindBindingActivityFactory(BindingComponent.Builder builder);

    /**
     * ContributesAndroidInjector用在方法中，此方法必须是无参数的，且返回值为Android中具体的组件(Activity，Fragment，Service等)，然后ContributesAndroidInjector
     * 根据方法的返回类型为其生成一个SubComponent，然后我们就不需要在写Component了
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = SplashModule.class)
    abstract SplashActivity bindSplashActivity();

    @Binds
    @IntoMap
    @ViewKey(CustomTextView.class)
    public abstract AndroidInjector.Factory<? extends View> bindCustomTextView(CustomTextViewComponent.Builder builder);

}