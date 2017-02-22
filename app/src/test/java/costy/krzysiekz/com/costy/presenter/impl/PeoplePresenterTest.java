package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.view.PeopleView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PeoplePresenterTest {

    private static final String PROJECT_NAME = "Some project";
    private static final String USER_NAME = "Kate";
    private static final String DEFAULT_CURRENCY = "EUR";

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
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject expenseProject = new ExpenseProject(PROJECT_NAME, defaultCurrency);
        User kate = new User(USER_NAME);
        expenseProject.addUser(kate);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(expenseProject);
        presenter.attachView(peopleView);
        presenter.loadProjectPeople(PROJECT_NAME);
        //then
        verify(peopleView).showPeople(Collections.singletonList(kate));
    }

    @Test
    public void shouldAddUserAndCallUpdate() {
        //given
        String personName = "John";
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject expenseProject = new ExpenseProject(PROJECT_NAME, defaultCurrency);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(expenseProject);
        presenter.attachView(peopleView);
        presenter.addPerson(PROJECT_NAME, personName);
        //then
        assertThat(expenseProject.getUsers()).isNotEmpty().extracting("name").containsOnly(personName);
        verify(repository).updateProject(expenseProject);
        verify(peopleView).showPeople(expenseProject.getUsers());
    }

    @Test
    public void shouldRemoveUsersFromProjectAndNotifyView() {
        //given
        User user = new User("John");
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject expenseProject = new ExpenseProject(PROJECT_NAME, defaultCurrency);
        expenseProject.addUser(user);
        Map<Integer, User> selected = new HashMap<>();
        selected.put(0, user);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(expenseProject);
        presenter.attachView(peopleView);
        presenter.removeUsers(PROJECT_NAME, selected);
        //then
        assertThat(expenseProject.getUsers()).isEmpty();
        verify(repository).updateProject(expenseProject);
        verify(peopleView).removePeople(new HashSet<>(Collections.singletonList(0)));
    }

    @Test
    public void shouldNotifyViewWhenUsersAreUsedInExpenses() {
        //given
        User john = new User("John");
        User kate = new User("Kate");
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject expenseProject = new ExpenseProject(PROJECT_NAME, defaultCurrency);
        expenseProject.addUser(john);
        expenseProject.addUser(kate);
        UserExpense expense = new UserExpense.UserExpenseBuilder().
                withUser(john).withAmount(new BigDecimal("10")).
                withReceivers(Arrays.asList(john, kate)).withDescription("Description").
                withCurrency(defaultCurrency).build();
        expenseProject.addExpense(expense);
        Map<Integer, User> selected = new HashMap<>();
        selected.put(0, kate);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(expenseProject);
        presenter.attachView(peopleView);
        presenter.removeUsers(PROJECT_NAME, selected);
        //then
        assertThat(expenseProject.getUsers()).hasSize(2).containsOnly(john, kate);
        verify(peopleView).showPeopleUsedInExpensesError();
    }
}