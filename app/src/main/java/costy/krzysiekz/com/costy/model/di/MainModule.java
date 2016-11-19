package costy.krzysiekz.com.costy.model.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    Application application;

    public MainModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }
}
