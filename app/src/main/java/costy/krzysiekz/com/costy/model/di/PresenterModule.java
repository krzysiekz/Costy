package costy.krzysiekz.com.costy.model.di;

import javax.inject.Singleton;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
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
    ExpensesPresenter provideExpensesPresenter() {
        return new ExpensesPresenter();
    }
}
