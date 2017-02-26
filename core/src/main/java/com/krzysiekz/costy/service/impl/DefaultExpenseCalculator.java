package com.krzysiekz.costy.service.impl;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;
import com.krzysiekz.costy.service.ExpenseCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultExpenseCalculator implements ExpenseCalculator {

    private static final int DECIMAL_PLACES = 3;

    @Override
    public ExpenseReport calculate(ExpenseProject expenseProject) {

        List<UserExpense> userExpenses = expenseProject.getExpenses();

        Map<Map.Entry<User, Currency>, Map<User, BigDecimal>> calculations = new HashMap<>();

        for (UserExpense userExpense : userExpenses) {
            List<User> receivers = userExpense.getReceivers();
            BigDecimal amountPerUser = userExpense.getAmount().
                    divide(new BigDecimal(receivers.size()), DECIMAL_PLACES, RoundingMode.HALF_UP);

            for (User u : receivers) {
                if (!userExpense.getUser().equals(u)) {
                    Map.Entry<User, Currency> entry = createEntry(userExpense.getUser(), userExpense.getCurrency());
                    if (calculations.containsKey(entry) && calculations.get(entry).containsKey(u)) {
                        BigDecimal amount = calculations.get(entry).get(u);
                        calculations.get(entry).put(u, amount.subtract(amountPerUser));
                    } else {
                        Map.Entry<User, Currency> userCurrencyEntry = createEntry(u, userExpense.getCurrency());
                        if (!calculations.containsKey(userCurrencyEntry)) {
                            calculations.put(userCurrencyEntry, new HashMap<>());
                        }

                        if (calculations.get(userCurrencyEntry).containsKey(userExpense.getUser())) {
                            BigDecimal amount = calculations.get(userCurrencyEntry).get(userExpense.getUser());
                            calculations.get(userCurrencyEntry).put(userExpense.getUser(),
                                    amount.add(amountPerUser));
                        } else {
                            calculations.get(userCurrencyEntry).put(userExpense.getUser(), amountPerUser);
                        }
                    }
                }
            }
        }

        return createReport(expenseProject, calculations);
    }

    private Map.Entry<User, Currency> createEntry(User user, Currency currency) {
        return new AbstractMap.SimpleEntry<>(user, currency);
    }

    private ExpenseReport createReport(ExpenseProject expenseProject,
                                       Map<Map.Entry<User, Currency>, Map<User, BigDecimal>> calculations) {
        ExpenseReport report = new ExpenseReport(expenseProject);
        for (Map.Entry<User, Currency> user : calculations.keySet()) {
            Map<User, BigDecimal> userExpenses = calculations.get(user);
            for (User userExpense : userExpenses.keySet()) {
                int compareTo = userExpenses.get(userExpense).compareTo(BigDecimal.ZERO);
                if (compareTo < 0) {
                    report.addEntry(new ReportEntry.Builder().
                            withSender(userExpense).
                            withReceiver(user.getKey()).
                            withAmount(userExpenses.get(userExpense).abs()).
                            withCurrency(user.getValue()).build());
                } else if (compareTo > 0) {
                    report.addEntry(new ReportEntry.Builder().
                            withSender(user.getKey()).
                            withReceiver(userExpense).
                            withAmount(userExpenses.get(userExpense)).
                            withCurrency(user.getValue()).build());
                }
            }
        }
        return report;
    }
}
