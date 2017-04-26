package costy.krzysiekz.com.costy.model.di;

import com.krzysiekz.costy.service.ExpenseCalculator;

import javax.inject.Singleton;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.model.report.converter.impl.ReportToTextConverter;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.presenter.impl.PeoplePresenter;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.presenter.impl.ReportPresenter;
import costy.krzysiekz.com.costy.presenter.impl.SelectedProjectPresenter;
import costy.krzysiekz.com.costy.presenter.impl.SettingsPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Singleton
    @Provides
    ProjectsPresenter provideProjectsPresenter(ProjectsRepository repository) {
        return new ProjectsPresenter(repository);
    }

    @Singleton
    @Provides
    ExpensesPresenter provideExpensesPresenter(ProjectsRepository repository) {
        return new ExpensesPresenter(repository);
    }

    @Singleton
    @Provides
    SelectedProjectPresenter provideSelectedProjectPresenter(ProjectsRepository repository) {
        return new SelectedProjectPresenter(repository);
    }

    @Singleton
    @Provides
    PeoplePresenter providePeoplePresenter(ProjectsRepository repository) {
        return new PeoplePresenter(repository);
    }

    @Singleton
    @Provides
    ReportPresenter provideReportPresenter(ProjectsRepository repository, ExpenseCalculator calculator,
                                           ReportToTextConverter reportConverter) {
        return new ReportPresenter(repository, calculator, reportConverter);
    }

    @Singleton
    @Provides
    SettingsPresenter provideSettingsPresenter(ProjectsRepository repository) {
        return new SettingsPresenter(repository);
    }
}
