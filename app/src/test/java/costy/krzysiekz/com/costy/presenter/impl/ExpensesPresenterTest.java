package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.UserExpense;

import org.junit.Test;

import java.util.Arrays;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.view.ExpensesView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpensesPresenterTest {
    @Test
    public void shouldCallViewWithProperObjectsWHenLoadingProject() {
        //given
        ExpensesView expensesView = mock(ExpensesView.class);
        ProjectsRepository repository = mock(ProjectsRepository.class);

        String projectName = "Project name";
        ExpenseProject project = new ExpenseProject(projectName);
        UserExpense userExpense = mock(UserExpense.class);
        UserExpense secondUserExpense = mock(UserExpense.class);
        project.addExpense(userExpense);
        project.addExpense(secondUserExpense);

        ExpensesPresenter presenter = new ExpensesPresenter(repository);
        //when
        when(repository.getProject(projectName)).thenReturn(project);
        presenter.attachView(expensesView);
        presenter.loadProjectExpenses(projectName);
        //then
        verify(repository).getProject(projectName);
        verify(expensesView).showExpenses(Arrays.asList(userExpense, secondUserExpense));
    }
}