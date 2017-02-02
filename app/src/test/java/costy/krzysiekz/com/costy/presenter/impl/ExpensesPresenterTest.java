package costy.krzysiekz.com.costy.presenter.impl;

import android.support.annotation.NonNull;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import org.junit.Before;
import org.junit.Test;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.view.ExpensesView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpensesPresenterTest {

    private static final String PROJECT_NAME = "Project name";

    private ExpensesView expensesView;
    private ProjectsRepository repository;

    @Before
    public void setUp() throws Exception {
        expensesView = mock(ExpensesView.class);
        repository = mock(ProjectsRepository.class);
    }

    @Test
    public void shouldCallViewWithProperObjectsWHenLoadingProject() {
        //given
        ExpenseProject project = createExpenseProject();
        ExpensesPresenter presenter = new ExpensesPresenter(repository);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(project);
        presenter.attachView(expensesView);
        presenter.loadProjectExpenses(PROJECT_NAME);
        //then
        verify(repository).getProject(PROJECT_NAME);
        verify(expensesView).showExpenses(project.getExpenses());
    }

    @Test
    public void shouldCallViewWithProperObjectsWhenShowingAddExpenseDialog() {
        //given
        ExpenseProject project = createExpenseProject();
        ExpensesPresenter presenter = new ExpensesPresenter(repository);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(project);
        presenter.attachView(expensesView);
        presenter.showAddExpenseDialog(PROJECT_NAME);
        //then
        verify(repository).getProject(PROJECT_NAME);
        verify(expensesView).showAddExpenseDialog(project.getUsers());
    }

    @NonNull
    private ExpenseProject createExpenseProject() {
        ExpenseProject project = new ExpenseProject(PROJECT_NAME);

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