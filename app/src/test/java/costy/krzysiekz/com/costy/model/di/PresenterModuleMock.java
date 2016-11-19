package costy.krzysiekz.com.costy.model.di;

import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;

import static org.mockito.Mockito.mock;

public class PresenterModuleMock extends PresenterModule {

    private ProjectsPresenter projectsPresenter;

    @Override
    ProjectsPresenter provideProjectsPresenter() {
        if (projectsPresenter == null) {
            projectsPresenter = mock(ProjectsPresenter.class);
        }
        return projectsPresenter;
    }

    public ProjectsPresenter getProjectsPresenter() {
        return projectsPresenter;
    }
}