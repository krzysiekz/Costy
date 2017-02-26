package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.Currency;
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
    private static final String DEFAULT_CURRENCY = "EUR";

    @Mock
    private ReportView reportView;

    @Mock
    private ProjectsRepository repository;

    @Captor
    private ArgumentCaptor<List<ReportEntry>> reportEntriesCaptor;

    @Test
    public void shouldCallViewWithProperObjectsWhenLoadingProject() {
        //given
        String expectedReportEntry = "Kate -> John: 20 EUR";
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
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject project = new ExpenseProject(PROJECT_NAME, defaultCurrency);

        User kate = new User("Kate");
        User john = new User("John");
        project.addUser(kate);
        project.addUser(john);

        UserExpense userExpense = new UserExpense.UserExpenseBuilder().
                withUser(kate).withAmount(new BigDecimal("10")).
                withReceivers(Arrays.asList(john, kate)).withDescription("Kate expense").
                withCurrency(defaultCurrency).build();

        UserExpense secondUserExpense = new UserExpense.UserExpenseBuilder().
                withUser(john).withAmount(new BigDecimal("50")).
                withReceivers(Arrays.asList(john, kate)).withDescription("John expense").
                withCurrency(defaultCurrency).build();

        project.addExpense(userExpense);
        project.addExpense(secondUserExpense);

        return project;
    }
}