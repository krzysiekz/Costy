package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.model.dao.impl.InMemoryProjectsRepository;
import costy.krzysiekz.com.costy.view.ProjectsView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProjectsPresenterTest {

    private ProjectsView projectsView;
    private ProjectsPresenter presenter;
    private ProjectsRepository repository;

    @Before
    public void setUp() throws Exception {
        //given
        repository = new InMemoryProjectsRepository();
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
        Currency defaultCurrency = new Currency("EUR");
        ExpenseProject expenseProject = new ExpenseProject("Some project", defaultCurrency);
        //when
        presenter.attachView(projectsView);
        presenter.addProject("Some project", defaultCurrency);
        //then
        verify(projectsView).showProjects(Collections.singletonList(expenseProject));
    }

    @Test
    public void shouldRemoveProjects() {
        //given
        Integer position = 0;
        Currency defaultCurrency = new Currency("EUR");
        ExpenseProject expenseProject = new ExpenseProject("Some project", defaultCurrency);
        Map<Integer, ExpenseProject> toRemove = new HashMap<>();
        toRemove.put(position, expenseProject);
        //when
        presenter.attachView(projectsView);
        presenter.addProject("Some project", defaultCurrency);
        presenter.removeProjects(toRemove);
        //then
        assertThat(repository.getAllProjects()).isEmpty();
        verify(projectsView).removeProjects(new HashSet<>(Collections.singletonList(position)));
    }

    @Test
    public void shouldLoadProjectsAndPassToView() {
        //given
        Currency defaultCurrency = new Currency("EUR");
        ExpenseProject expenseProject = new ExpenseProject("Some project", defaultCurrency);
        //when
        repository.addProject("Some project", defaultCurrency);
        presenter.attachView(projectsView);
        presenter.loadProjects();
        //then
        verify(projectsView).showProjects(Collections.singletonList(expenseProject));
    }
}