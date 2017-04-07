package costy.krzysiekz.com.costy.presenter.impl;

import android.support.annotation.NonNull;

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
import java.util.List;
import java.util.Map;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.view.ExpensesView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpensesPresenterTest {

    private static final String PROJECT_NAME = "Project name";
    private static final String DEFAULT_CURRENCY = "EUR";

    private ExpensesView expensesView;
    private ProjectsRepository repository;
    private ExpensesPresenter presenter;

    @Before
    public void setUp() throws Exception {
        expensesView = mock(ExpensesView.class);
        repository = mock(ProjectsRepository.class);
        presenter = new ExpensesPresenter(repository);
    }

    @Test
    public void shouldCallViewWithProperObjectsWhenLoadingProject() {
        //given
        ExpenseProject project = createExpenseProject();
        //when
        when(repository.getAllExpenses(PROJECT_NAME)).thenReturn(project.getExpenses());
        presenter.attachView(expensesView);
        presenter.loadProjectExpenses(PROJECT_NAME);
        //then
        verify(repository).getAllExpenses(PROJECT_NAME);
        verify(expensesView).showExpenses(project.getExpenses());
    }

    @Test
    public void shouldCallViewWithProperObjectsWhenShowingAddExpenseDialog() {
        //given
        ExpenseProject project = createExpenseProject();
        List<Currency> currencies = Collections.singletonList(project.getDefaultCurrency());
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(project);
        when(repository.getAllCurrencies()).thenReturn(currencies);
        presenter.attachView(expensesView);
        presenter.showAddExpenseDialog(PROJECT_NAME);
        //then
        verify(repository).getProject(PROJECT_NAME);
        verify(expensesView).showAddExpenseDialog(project.getUsers(), currencies,
                project.getDefaultCurrency());
    }

    @Test
    public void shouldAddExpenseToProjectAndCallUpdate() {
        //given
        User john = new User("John");
        User kate = new User("Kate");
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        UserExpense expense = new UserExpense.UserExpenseBuilder().
                withUser(john).withAmount(new BigDecimal("10")).
                withReceivers(Arrays.asList(john, kate)).withDescription("Sample expense").
                withCurrency(defaultCurrency).build();
        //when
        when(repository.getAllExpenses(PROJECT_NAME)).thenReturn(Collections.singletonList(expense));
        presenter.attachView(expensesView);
        presenter.addExpense(PROJECT_NAME, expense);
        //then
        verify(repository).addExpense(PROJECT_NAME, expense);
        verify(expensesView).showExpenses(Collections.singletonList(expense));
    }

    @Test
    public void shouldRemoveExpensesFromProjectAndCallUpdate() {
        //given
        User john = new User("John");
        User kate = new User("Kate");
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        UserExpense expense = new UserExpense.UserExpenseBuilder().
                withUser(john).withAmount(new BigDecimal("10")).
                withReceivers(Arrays.asList(john, kate)).withDescription("Sample expense").
                withCurrency(defaultCurrency).build();
        Map<Integer, UserExpense> selected = new HashMap<>();
        selected.put(0, expense);
        //when
        presenter.attachView(expensesView);
        presenter.removeExpenses(PROJECT_NAME, selected);
        //then
        verify(repository).removeExpenses(PROJECT_NAME, selected.keySet());
        verify(expensesView).removeExpenses(new HashSet<>(Collections.singletonList(0)));
    }

    @NonNull
    private ExpenseProject createExpenseProject() {
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject project = new ExpenseProject(PROJECT_NAME, defaultCurrency);

        UserExpense userExpense = mock(UserExpense.class);
        UserExpense secondUserExpense = mock(UserExpense.class);
        project.addExpense(userExpense);
        project.addExpense(secondUserExpense);

        User kate = new User("Kate");
        User john = new User("John");
        project.addUser(kate);
        project.addUser(john);

        return project;
    }
}