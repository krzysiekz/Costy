package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectedProjectPresenterTest {

    private static final String PROJECT_NAME = "Project Name";
    private static final String CURRENCY = "EUR";

    private ProjectsRepository repository;
    private ExpenseProject project;
    private SelectedProjectPresenter presenter;

    @Before
    public void setUp() throws Exception {
        repository = mock(ProjectsRepository.class);
        project = new ExpenseProject(PROJECT_NAME, new Currency(CURRENCY));
        presenter = new SelectedProjectPresenter(repository);
    }

    @Test
    public void shouldReturnTrueIfPeopleInProject() {
        //given
        User john = new User("John");
        //when
        when(repository.getAllUsers(PROJECT_NAME)).thenReturn(Collections.singletonList(john));
        Boolean result = presenter.checkIfPeopleAdded(PROJECT_NAME);
        //then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfNoPeopleInProject() {
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(project);
        Boolean result = presenter.checkIfPeopleAdded(PROJECT_NAME);
        //then
        assertThat(result).isFalse();
    }
}