package costy.krzysiekz.com.costy.model.di;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.presenter.impl.PeoplePresenter;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.presenter.impl.ReportPresenter;
import costy.krzysiekz.com.costy.presenter.impl.SelectedProjectPresenter;

import static org.mockito.Mockito.mock;

public class PresenterModuleMock extends PresenterModule {

    private ProjectsPresenter projectsPresenter;
    private ExpensesPresenter expensesPresenter;
    private SelectedProjectPresenter selectedProjectPresenter;
    private PeoplePresenter peoplePresenter;
    private ReportPresenter reportPresenter;

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

    @Override
    PeoplePresenter providePeoplePresenter(ProjectsRepository projectsRepository) {
        if (peoplePresenter == null) {
            peoplePresenter = mock(PeoplePresenter.class);
        }
        return peoplePresenter;
    }

    @Override
    ReportPresenter provideReportPresenter(ProjectsRepository repository) {
        if (reportPresenter == null) {
            reportPresenter = mock(ReportPresenter.class);
        }
        return reportPresenter;
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

    public PeoplePresenter getPeoplePresenter() {
        return peoplePresenter;
    }

    public void setSelectedProjectPresenter(SelectedProjectPresenter selectedProjectPresenter) {
        this.selectedProjectPresenter = selectedProjectPresenter;
    }

    public ReportPresenter getReportPresenter() {
        return reportPresenter;
    }
}