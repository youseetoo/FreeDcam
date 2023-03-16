package hilt;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.components.SingletonComponent;
import uc2.uc2rest.RestController;

@Module
@InstallIn(SingletonComponent.class)
public class Uc2RestModule {
    @Provides
    @Singleton
    public static RestController restController()
    {
        return new RestController();
    }
}