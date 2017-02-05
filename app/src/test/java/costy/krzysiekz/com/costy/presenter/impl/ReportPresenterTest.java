package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;
import com.krzysiekz.costy.service.ExpenseCalculator;
import com.krzysiekz.costy.service.impl.DefaultExpenseCalculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.view.ReportView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportPresenterTest {

    private static final String PROJECT_NAME = "Project name";

    @Mock
    private ReportView reportView;

    @Mock
    private ProjectsRepository repository;

    @Captor
    private ArgumentCaptor<List<ReportEntry>> reportEntriesCaptor;

    @Test
    public void shouldCallViewWithProperObjectsWhenLoadingProject() {
        //given
        String expectedReportEntry = "Kate -> John: 20";
        ExpenseProject project = createExpenseProject();
        ExpenseCalculator calculator = new DefaultExpenseCalculator();
        ReportPresenter presenter = new ReportPresenter(repository, calculator);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(project);
        presenter.attachView(reportView);
        presenter.loadReportEntries(PROJECT_NAME);
        //then
        verify(repository).getProject(PROJECT_NAME);
        verify(reportView).showReportEntries(reportEntriesCaptor.capture());
        assertThat(reportEntriesCaptor.getValue()).hasSize(1).
                extracting(ReportEntry::toString).
                containsOnly(expectedReportEntry);
    }

    private ExpenseProject createExpenseProject() {
        ExpenseProject project = new ExpenseProject(PROJECT_NAME);

        User kate = new User("Kate");
        User john = new User("John");
        project.addUser(kate);
        project.addUser(john);

        UserExpense userExpense = new UserExpense(kate, new BigDecimal("10"), Arrays.asList(kate, john), "Kate expense");
        UserExpense secondUserExpense = new UserExpense(john, new BigDecimal("50"), Arrays.asList(kate, john), "John expense");
        project.addExpense(userExpense);
        project.addExpense(secondUserExpense);

        return project;
    }
}