package com.krzysiekz.costy.service;

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

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ExpenseCalculatorTest {

    private ExpenseProject expenseProject;
    private ExpenseCalculator expenseCalculator;

    @Before
    public void setUp() throws Exception {
        //given
        expenseProject = new ExpenseProject("Test Project");
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
        StreamSupport.stream(expenses).forEach(e -> expenseProject.addExpense(e));
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
                reportShouldBeValidWhenUserIsNotPayingForAllOtherUsers()
        };
    }

    private Object[] reportShouldBeValidWhenUserIsNotPayingForAllOtherUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 15, Arrays.asList("John", "Kate")),
                        createExpense("Kate", 30, Arrays.asList("John", "Kate", "Bob")),
                        createExpense("Bob", 45, Arrays.asList("John", "Kate", "Bob"))
                ),
                Arrays.asList("John -> Kate: 2,5",
                        "Kate -> Bob: 5",
                        "John -> Bob: 15")
        };
    }

    private Object[] reportShouldBeValidForMultipleUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 9, Arrays.asList("John", "Kate", "Bob")),
                        createExpense("Kate", 18, Arrays.asList("John", "Kate", "Bob")),
                        createExpense("Bob", 27, Arrays.asList("John", "Kate", "Bob"))
                ),
                Arrays.asList("John -> Kate: 3",
                        "Kate -> Bob: 3",
                        "John -> Bob: 6")
        };
    }

    private Object[] reportShouldBeEmptyWhenExpensesAreTheSameForAllUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 50, Arrays.asList("John", "Kate")),
                        createExpense("Kate", 50, Arrays.asList("John", "Kate"))
                ),
                new ArrayList<>()
        };
    }

    private Object[] reportShouldBeEmptyWhenExpensesAreTheSameForAllUsers2() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 50, Arrays.asList("John", "Kate", "Bob")),
                        createExpense("Kate", 50, Arrays.asList("John", "Kate", "Bob")),
                        createExpense("Bob", 50, Arrays.asList("John", "Kate", "Bob"))
                ),
                new ArrayList<>()
        };
    }

    private Object[] reportShouldBeValidForTwoUsers() {
        return new Object[]{
                Arrays.asList(
                        createExpense("John", 50, Arrays.asList("John", "Kate")),
                        createExpense("Kate", 20, Arrays.asList("John", "Kate"))
                ),
                Collections.singletonList("Kate -> John: 15")
        };
    }

    private Object[] reportForSingleUserShouldBeEmpty() {
        return new Object[]{
                Collections.singletonList(
                        createExpense("John", 10, Collections.singletonList("John"))
                ),
                new ArrayList<>()
        };
    }

    private UserExpense createExpense(String userName, int amount, List<String> receivers) {
        User john = new User(userName);
        List<User> userList = StreamSupport.stream(receivers).map(User::new).collect(Collectors.toList());
        return new UserExpense(john, new BigDecimal(amount), userList);
    }
}
