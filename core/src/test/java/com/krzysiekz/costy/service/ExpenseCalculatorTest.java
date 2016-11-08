package com.krzysiekz.costy.service;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;
import com.krzysiekz.costy.service.impl.DefaultExpenseCalculator;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    public void shouldCalculateExpensesForOneUser() throws Exception {
        //given
        UserExpense johnExpense = createExpense("John", 10);
        //when
        expenseProject.addExpense(johnExpense);
        ExpenseReport expenseReport = expenseCalculator.calculate(expenseProject);
        //then
        assertThat(expenseReport.getEntries()).isEmpty();
    }

    @Test
    public void shouldCalculateExpensesForTwoUsers() throws Exception {
        //given
        UserExpense johnExpense = createExpense("John", 50);
        UserExpense kateExpense = createExpense("Kate", 20);
        //when
        expenseProject.addExpense(johnExpense);
        expenseProject.addExpense(kateExpense);
        ExpenseReport expenseReport = expenseCalculator.calculate(expenseProject);
        //then
        assertThat(expenseReport.getEntries()).hasSize(1);
        assertThat(expenseReport.getEntries()).extracting("sender", "receiver", "amount").
                contains(tuple(kateExpense.getUser(), johnExpense.getUser(), new BigDecimal("15.000")));
    }

    @Test
    public void shouldReturnEmptyReportWhenUsersExpensesAreTheSame() throws Exception {
        //given
        UserExpense johnExpense = createExpense("John", 50);
        UserExpense kateExpense = createExpense("Kate", 50);
        //when
        expenseProject.addExpense(johnExpense);
        expenseProject.addExpense(kateExpense);
        ExpenseReport expenseReport = expenseCalculator.calculate(expenseProject);
        //then
        assertThat(expenseReport.getEntries()).isEmpty();
    }

    @Test
    public void shouldReturnProperReportForMultipleUsers() {
        //given
        UserExpense johnExpense = createExpense("John", 9);
        UserExpense kateExpense = createExpense("Kate", 18);
        UserExpense bobExpense = createExpense("Bob", 27);
        //when
        expenseProject.addExpense(johnExpense);
        expenseProject.addExpense(kateExpense);
        expenseProject.addExpense(bobExpense);
        ExpenseReport expenseReport = expenseCalculator.calculate(expenseProject);
        //then
        assertThat(expenseReport.getEntries()).hasSize(3);
        assertThat(expenseReport.getEntries()).extracting("sender", "receiver", "amount").
                containsOnly(
                        tuple(johnExpense.getUser(), kateExpense.getUser(), new BigDecimal("3.000")),
                        tuple(johnExpense.getUser(), bobExpense.getUser(), new BigDecimal("6.000")),
                        tuple(kateExpense.getUser(), bobExpense.getUser(), new BigDecimal("3.000"))
                );
    }

    private UserExpense createExpense(String userName, int amount) {
        User john = new User(userName);
        return new UserExpense(john, new BigDecimal(amount));
    }
}
