package costy.krzysiekz.com.costy.model.di;

import android.app.Application;

import com.krzysiekz.costy.service.ExpenseCalculator;
import com.krzysiekz.costy.service.impl.DefaultExpenseCalculator;

import javax.inject.Singleton;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.model.dao.impl.InMemoryProjectsRepository;
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

    @Provides
    @Singleton
    ProjectsRepository provideProjectsRepository() {
        return new InMemoryProjectsRepository();
    }

    @Provides
    @Singleton
    ExpenseCalculator provideExpenseCalculator() {
        return new DefaultExpenseCalculator();
    }
}
