package me.ztiany.compose.facility.data;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.ServiceContext;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

/**
 * @author Ztiany
 */
@Module
@InstallIn(ActivityRetainedComponent.class)
public class DataModule {

    @ActivityRetainedScoped
    @Provides
    static ServiceContext<WanAndroidApi> provideWanAndroidApi() {
        return NetContext.get().serviceFactory(NetContext.DEFAULT_CONFIG).createServiceContext((WanAndroidApi.class));
    }

}