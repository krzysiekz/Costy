package com.krzysiekz.costy.service.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.UserExpense;
import com.krzysiekz.costy.service.ExpenseCalculator;

import java.math.BigDecimal;
import java.util.List;

public class DefaultExpenseCalculator implements ExpenseCalculator {

    @Override
    public ExpenseReport calculate(ExpenseProject expenseProject) {
        ExpenseReport report = new ExpenseReport(expenseProject);

        List<UserExpense> userExpenses = expenseProject.getExpenses();

        if (userExpenses.size() == 2) {
            UserExpense userExpense = userExpenses.get(0);
            UserExpense userExpense1 = userExpenses.get(1);
            if (userExpense.getAmount().compareTo(userExpense1.getAmount()) < 0) {
                ReportEntry entry =
                        new ReportEntry(userExpense.getUser(), userExpense1.getUser(),
                                userExpense1.getAmount().divide(new BigDecimal(2)).subtract(
                                        userExpense.getAmount().divide(new BigDecimal(2))
                                ));
                report.addEntry(entry);
            } else if (userExpense.getAmount().compareTo(userExpense1.getAmount()) > 0) {
                ReportEntry entry =
                        new ReportEntry(userExpense1.getUser(), userExpense.getUser(),
                                userExpense.getAmount().divide(new BigDecimal(2)).subtract(
                                        userExpense1.getAmount().divide(new BigDecimal(2))
                                ));
                report.addEntry(entry);
            }
        }

        return report;
    }
}
