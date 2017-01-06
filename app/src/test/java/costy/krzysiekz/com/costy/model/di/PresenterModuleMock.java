package costy.krzysiekz.com.costy.model.di;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.presenter.impl.SelectedProjectPresenter;

import static org.mockito.Mockito.mock;

public class PresenterModuleMock extends PresenterModule {

    private ProjectsPresenter projectsPresenter;
    private ExpensesPresenter expensesPresenter;
    private SelectedProjectPresenter selectedProjectPresenter;

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

    @Override
    SelectedProjectPresenter provideSelectedProjectPresenter(ProjectsRepository projectsRepository) {
        if (selectedProjectPresenter == null) {
            selectedProjectPresenter = mock(SelectedProjectPresenter.class);
        }
        return selectedProjectPresenter;
    }

    public ProjectsPresenter getProjectsPresenter() {
        return projectsPresenter;
    }

    public ExpensesPresenter getExpensesPresenter() {
        return expensesPresenter;
    }

    public SelectedProjectPresenter getSelectedProjectPresenter() {
        return selectedProjectPresenter;
    }

    public void setSelectedProjectPresenter(SelectedProjectPresenter selectedProjectPresenter) {
        this.selectedProjectPresenter = selectedProjectPresenter;
    }
}