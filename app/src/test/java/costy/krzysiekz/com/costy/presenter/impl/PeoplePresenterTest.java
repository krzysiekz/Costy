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
    public void shouldCallViewWhenAddingPerson() {
        //given
        User kate = new User(USER_NAME);
        //when
        when(repository.getAllUsers(PROJECT_NAME)).thenReturn(Collections.singletonList(kate));
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
        verify(repository).addPerson(PROJECT_NAME, new User(personName));
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
        verify(repository).removeUsers(PROJECT_NAME, selected.values());
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