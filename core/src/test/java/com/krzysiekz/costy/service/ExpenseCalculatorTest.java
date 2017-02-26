package com.krzysiekz.costy.service;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;
import com.krzysiekz.costy.service.impl.DefaultExpenseCalculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ExpenseCalculatorTest {

    private ExpenseProject expenseProject;
    private ExpenseCalculator expenseCalculator;
    private Currency defaultCurrency =  new Currency("EUR");

    @Before
    public void setUp() throws Exception {
        //given
        expenseProject = new ExpenseProject("Test Project", defaultCurrency);
        expenseCalculator = new DefaultExpenseCalculator();
    }

    @Test
    public void shouldReturnReportWithProperProject() throws Exception {
        //when
        ExpenseReport expenseReport = expenseCalculator.calculate(expenseProject);
        //then
        assertThat(expenseReport.getProject()).isEqualTo(expenseProject);
    }

    @Test
    @Parameters(method = "prepareTestCases")
    public void shouldCreateProperReport(List<UserExpense> expenses, List<String> expectedEntries) {
        //when
        expenses.stream().forEach(e -> expenseProject.addExpense(e));
        ExpenseReport expenseReport = expenseCalculator.calculate(expenseProject);
        //then
        assertThat(expenseReport.getEntries()).hasSize(expectedEntries.size());
        assertThat(expenseReport.getEntries()).extracting(ReportEntry::toString).
                containsAll(expectedEntries);
    }

    private Object prepareTestCases() {
        return new Object[]{
                reportForSingleUserShouldBeEmpty(),
                reportShouldBeValidForTwoUsers(),
                reportShouldBeEmptyWhenExpensesAreTheSameForAllUsers(),
                reportShouldBeEmptyWhenExpensesAreTheSameForAllUsers2(),
                reportShouldBeValidForMultipleUsers(),
                reportShouldBeValidWhenUserIsNotPayingForAllOtherUsers(),
                reportShouldBeValidForTwoUsersAndTwoCurrencies(),
                reportShouldBeValidForMultipleUsersAndMultipleCurrencies()
        };
    }

    private Object[] reportShouldBeValidWhenUserIsNotPayingForAllOtherUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 15, Arrays.asList("John", "Kate"), defaultCurrency),
                        createExpense("Kate", 30, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("Bob", 45, Arrays.asList("John", "Kate", "Bob"), defaultCurrency)
                ),
                Arrays.asList("John -> Kate: 2.5 EUR",
                        "Kate -> Bob: 5 EUR",
                        "John -> Bob: 15 EUR")
        };
    }

    private Object[] reportShouldBeValidForMultipleUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 9, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("Kate", 18, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("Bob", 27, Arrays.asList("John", "Kate", "Bob"), defaultCurrency)
                ),
                Arrays.asList("John -> Kate: 3 EUR",
                        "Kate -> Bob: 3 EUR",
                        "John -> Bob: 6 EUR")
        };
    }

    private Object[] reportShouldBeValidForMultipleUsersAndMultipleCurrencies() {
        Currency pln = new Currency("PLN");
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 9, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("Kate", 18, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("Bob", 27, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("John", 9, Arrays.asList("John", "Kate", "Bob"), pln),
                        createExpense("Kate", 18, Arrays.asList("John", "Kate", "Bob"), pln),
                        createExpense("Bob", 27, Arrays.asList("John", "Kate", "Bob"), pln)
                ),
                Arrays.asList("John -> Kate: 3 EUR",
                        "Kate -> Bob: 3 EUR",
                        "John -> Bob: 6 EUR",
                        "John -> Kate: 3 PLN",
                        "Kate -> Bob: 3 PLN",
                        "John -> Bob: 6 PLN")
        };
    }

    private Object[] reportShouldBeEmptyWhenExpensesAreTheSameForAllUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 50, Arrays.asList("John", "Kate"), defaultCurrency),
                        createExpense("Kate", 50, Arrays.asList("John", "Kate"), defaultCurrency)
                ),
                new ArrayList<>()
        };
    }

    private Object[] reportShouldBeEmptyWhenExpensesAreTheSameForAllUsers2() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 50, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("Kate", 50, Arrays.asList("John", "Kate", "Bob"), defaultCurrency),
                        createExpense("Bob", 50, Arrays.asList("John", "Kate", "Bob"), defaultCurrency)
                ),
                new ArrayList<>()
        };
    }

    private Object[] reportShouldBeValidForTwoUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 50, Arrays.asList("John", "Kate"), defaultCurrency),
                        createExpense("Kate", 20, Arrays.asList("John", "Kate"), defaultCurrency)
                ),
                Collections.singletonList("Kate -> John: 15 EUR")
        };
    }

    private Object[] reportShouldBeValidForTwoUsersAndTwoCurrencies() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 50, Arrays.asList("John", "Kate"), defaultCurrency),
                        createExpense("Kate", 20, Arrays.asList("John", "Kate"), new Currency("PLN"))
                ),
                Arrays.asList("Kate -> John: 25 EUR",
                        "John -> Kate: 10 PLN")
        };
    }

    private Object[] reportForSingleUserShouldBeEmpty() {
        return new Object[]{
                Collections.singletonList(
                        createExpense("John", 10, Collections.singletonList("John"), defaultCurrency)
                ),
                new ArrayList<>()
        };
    }

    private UserExpense createExpense(String userName, int amount, List<String> receivers,
                                      Currency currency) {
        User john = new User(userName);
        List<User> userList = receivers.stream().map(User::new).collect(Collectors.toList());

        return new UserExpense.UserExpenseBuilder().
                withUser(john).withAmount(new BigDecimal(amount)).
                withReceivers(userList).withDescription("").withCurrency(currency).build();
    }
}
