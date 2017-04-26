package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
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
import costy.krzysiekz.com.costy.model.report.converter.impl.ReportToTextConverter;
import costy.krzysiekz.com.costy.view.ReportView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
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

    @Mock
    private ReportToTextConverter reportConverter;

    @Captor
    private ArgumentCaptor<List<ReportEntry>> reportEntriesCaptor;

    @Test
    public void shouldCallViewWithProperObjectsWhenLoadingProject() {
        //given
        String expectedReportEntry = "Kate -> John: 20 EUR";
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject project = new ExpenseProject(PROJECT_NAME, defaultCurrency);

        User kate = new User("Kate");
        User john = new User("John");
        project.addUser(kate);
        project.addUser(john);

        List<User> receivers = Arrays.asList(john, kate);

        UserExpense userExpense = getUserExpense(defaultCurrency, kate, new BigDecimal("10"), receivers);
        UserExpense secondUserExpense = getUserExpense(defaultCurrency, john, new BigDecimal("50"), receivers);

        project.addExpense(userExpense);
        project.addExpense(secondUserExpense);

        ExpenseCalculator calculator = new DefaultExpenseCalculator();
        ReportPresenter presenter = new ReportPresenter(repository, calculator, reportConverter);
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

    @Test
    public void shouldSortReportEntriesByCurrencyAndUserBeforeSendingThenToView() {
        //given
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject project = new ExpenseProject(PROJECT_NAME, defaultCurrency);

        User kate = new User("Kate");
        User john = new User("John");
        User alex = new User("Alex");
        project.addUser(kate);
        project.addUser(john);
        project.addUser(alex);

        List<User> receivers = Arrays.asList(john, kate, alex);

        UserExpense userExpense = getUserExpense(defaultCurrency, kate, new BigDecimal("12"), receivers);
        UserExpense secondExpense = getUserExpense(new Currency("PLN"), john, new BigDecimal("12"), Arrays.asList(john, kate));
        UserExpense thirdExpense = getUserExpense(new Currency("PLN"), alex, new BigDecimal("12"), Arrays.asList(john, kate));

        project.addExpense(userExpense);
        project.addExpense(secondExpense);
        project.addExpense(thirdExpense);

        ExpenseCalculator calculator = new DefaultExpenseCalculator();
        ReportPresenter presenter = new ReportPresenter(repository, calculator, reportConverter);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(project);
        presenter.attachView(reportView);
        presenter.loadReportEntries(PROJECT_NAME);
        //then
        verify(repository).getProject(PROJECT_NAME);
        verify(reportView).showReportEntries(reportEntriesCaptor.capture());
        assertThat(reportEntriesCaptor.getValue()).hasSize(5).
                extracting(ReportEntry::toString).
                containsSequence("Alex -> Kate: 4 EUR",
                        "John -> Kate: 4 EUR",
                        "John -> Alex: 6 PLN",
                        "Kate -> Alex: 6 PLN",
                        "Kate -> John: 6 PLN");
    }

    @Test
    public void shouldPassConvertedReportToView() {
        //given
        Currency defaultCurrency = new Currency(DEFAULT_CURRENCY);
        ExpenseProject project = new ExpenseProject(PROJECT_NAME, defaultCurrency);
        ExpenseCalculator calculator = mock(ExpenseCalculator.class);
        ReportPresenter presenter = new ReportPresenter(repository, calculator, reportConverter);
        ExpenseReport report = new ExpenseReport(project);
        //when
        when(repository.getProject(PROJECT_NAME)).thenReturn(project);
        when(calculator.calculate(project)).thenReturn(report);
        when(reportConverter.convert(report)).thenReturn("Some converted report");
        presenter.attachView(reportView);
        presenter.shareReport(PROJECT_NAME);
        //then
        verify(reportView).shareReport(PROJECT_NAME, "Some converted report");
    }

    private UserExpense getUserExpense(Currency defaultCurrency, User kate, BigDecimal amount, List<User> receivers) {
        return new UserExpense.UserExpenseBuilder().
                withUser(kate).withAmount(amount).
                withReceivers(receivers).withDescription("").
                withCurrency(defaultCurrency).build();
    }
}