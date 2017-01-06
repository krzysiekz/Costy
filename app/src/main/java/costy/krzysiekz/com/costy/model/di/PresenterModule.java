package costy.krzysiekz.com.costy.model.di;

import javax.inject.Singleton;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.presenter.impl.SelectedProjectPresenter;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
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
}
