package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.view.PeopleView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PeoplePresenterTest {

    private static final String PROJECT_NAME = "Some project";
    private static final String USER_NAME = "Kate";

    private PeopleView peopleView;
    private PeoplePresenter presenter;
    private ProjectsRepository repository;

    @Before
    public void setUp() throws Exception {
        //given
        repository = mock(ProjectsRepository.class);
        peopleView = mock(PeopleView.class);
        presenter = new PeoplePresenter(repository);
    }

    @Test
    public void shouldCallRepositoryWhenAddingPerson() {
        //given
        ExpenseProject expenseProject = new ExpenseProject(PROJECT_NAME);
        User kate = new User(USER_NAME);
        expenseProject.addUser(kate);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(expenseProject);
        presenter.attachView(peopleView);
        presenter.loadProjectPeople(PROJECT_NAME);
        //then
        verify(peopleView).showPeople(Collections.singletonList(kate));
    }
}