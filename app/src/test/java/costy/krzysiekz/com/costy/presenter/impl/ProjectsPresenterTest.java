package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.ExpenseProject;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.model.dao.impl.InMemoryProjectsRepository;
import costy.krzysiekz.com.costy.view.ProjectsView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProjectsPresenterTest {

    private ProjectsView projectsView;
    private ProjectsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        //given
        ProjectsRepository repository = new InMemoryProjectsRepository();
        projectsView = mock(ProjectsView.class);
        presenter = new ProjectsPresenter(repository);
    }

    @Test
    public void shouldAttachView() {
        //when
        presenter.attachView(projectsView);
        //then
        assertThat(presenter.getProjectsView()).isEqualTo(projectsView);
    }

    @Test
    public void shouldDetachView() {
        //when
        presenter.attachView(projectsView);
        presenter.detachView();
        //then
        assertThat(presenter.getProjectsView()).isNull();
    }

    @Test
    public void shouldCallRepositoryWhenAddingProject() {
        //given
        ExpenseProject expenseProject = new ExpenseProject("Some project");
        //when
        presenter.attachView(projectsView);
        presenter.addProject("Some project");
        //then
        verify(projectsView).showProjects(Collections.singletonList(expenseProject));
    }
}