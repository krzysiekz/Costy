package costy.krzysiekz.com.costy.model.di;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;

import static org.mockito.Mockito.mock;

public class PresenterModuleMock extends PresenterModule {

    private ProjectsPresenter projectsPresenter;
    private ExpensesPresenter expensesPresenter;

    @Override
    ProjectsPresenter provideProjectsPresenter(ProjectsRepository projectsRepository) {
        if (projectsPresenter == null) {
            projectsPresenter = mock(ProjectsPresenter.class);
        }
        return projectsPresenter;
    }

    @Override
    ExpensesPresenter provideExpensesPresenter(ProjectsRepository projectsRepository) {
        if (expensesPresenter == null) {
            expensesPresenter = mock(ExpensesPresenter.class);
        }
        return expensesPresenter;
    }

    public ProjectsPresenter getProjectsPresenter() {
        return projectsPresenter;
    }

    public ExpensesPresenter getExpensesPresenter() {
        return expensesPresenter;
    }
}