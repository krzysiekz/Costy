package costy.krzysiekz.com.costy.model.di;

import android.app.Application;

import com.krzysiekz.costy.service.ExpenseCalculator;
import com.krzysiekz.costy.service.impl.DefaultExpenseCalculator;

import javax.inject.Singleton;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.model.dao.converter.impl.CurrencyConverter;
import costy.krzysiekz.com.costy.model.dao.converter.impl.ExpenseProjectConverter;
import costy.krzysiekz.com.costy.model.dao.converter.impl.UserConverter;
import costy.krzysiekz.com.costy.model.dao.impl.SQLLiteProjectsRepository;
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
    CurrencyConverter provideCurrencyConverter() {
        return new CurrencyConverter();
    }

    @Provides
    @Singleton
    UserConverter provideUserConverter() {
        return new UserConverter();
    }

    @Provides
    @Singleton
    ExpenseProjectConverter provideExpenseProjectConverter() {
        return new ExpenseProjectConverter();
    }

    @Provides
    @Singleton
    ProjectsRepository provideProjectsRepository(CurrencyConverter currencyConverter,
                                                 UserConverter userConverter,
                                                 ExpenseProjectConverter expenseProjectConverter) {
        SQLLiteProjectsRepository sqlLiteProjectsRepository = new SQLLiteProjectsRepository();
        sqlLiteProjectsRepository.setCurrencyConverter(currencyConverter);
        sqlLiteProjectsRepository.setUserConverter(userConverter);
        sqlLiteProjectsRepository.setExpenseProjectConverter(expenseProjectConverter);
        return sqlLiteProjectsRepository;
    }

    @Provides
    @Singleton
    ExpenseCalculator provideExpenseCalculator() {
        return new DefaultExpenseCalculator();
    }
}
