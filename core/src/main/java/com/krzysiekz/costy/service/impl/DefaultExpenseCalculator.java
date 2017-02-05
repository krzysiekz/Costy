package com.krzysiekz.costy.service.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;
import com.krzysiekz.costy.service.ExpenseCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultExpenseCalculator implements ExpenseCalculator {

    private static final int DECIMAL_PLACES = 3;

    @Override
    public ExpenseReport calculate(ExpenseProject expenseProject) {

        List<UserExpense> userExpenses = expenseProject.getExpenses();

        Map<User, Map<User, BigDecimal>> calculations = new HashMap<>();

        for (UserExpense userExpense : userExpenses) {
            List<User> receivers = userExpense.getReceivers();
            BigDecimal amountPerUser = userExpense.getAmount().
                    divide(new BigDecimal(receivers.size()), DECIMAL_PLACES, RoundingMode.HALF_UP);

            for (User u : receivers) {
                if (!userExpense.getUser().equals(u)) {
                    if (calculations.containsKey(userExpense.getUser())
                            && calculations.get(userExpense.getUser()).containsKey(u)) {
                        BigDecimal amount = calculations.get(userExpense.getUser()).get(u);
                        calculations.get(userExpense.getUser()).put(u, amount.subtract(amountPerUser));
                    } else {
                        if (!calculations.containsKey(u)) {
                            calculations.put(u, new HashMap<>());
                        }

                        if (calculations.get(u).containsKey(userExpense.getUser())) {
                            BigDecimal amount = calculations.get((u)).get(userExpense.getUser());
                            calculations.get(u).put(userExpense.getUser(),
                                    amount.add(amountPerUser));
                        } else {
                            calculations.get(u).put(userExpense.getUser(), amountPerUser);
                        }
                    }
                }
            }
        }

        return createReport(expenseProject, calculations);
    }

    private ExpenseReport createReport(ExpenseProject expenseProject,
                                       Map<User, Map<User, BigDecimal>> calculations) {
        ExpenseReport report = new ExpenseReport(expenseProject);
        for (User user : calculations.keySet()) {
            Map<User, BigDecimal> userExpenses = calculations.get(user);
            for (User userExpense : userExpenses.keySet()) {
                int compareTo = userExpenses.get(userExpense).compareTo(BigDecimal.ZERO);
                if (compareTo < 0) {
                    report.addEntry(new ReportEntry(userExpense, user, userExpenses.get(userExpense).abs()));
                } else if (compareTo > 0) {
                    report.addEntry(new ReportEntry(user, userExpense, userExpenses.get(userExpense)));
                }
            }
        }
        return report;
    }
}
