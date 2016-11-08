package com.krzysiekz.costy.service.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;
import com.krzysiekz.costy.service.ExpenseCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class DefaultExpenseCalculator implements ExpenseCalculator {

    public static final int DECIMAL_PLACES = 3;

    @Override
    public ExpenseReport calculate(ExpenseProject expenseProject) {

        List<UserExpense> userExpenses = expenseProject.getExpenses();
        List<User> allUsers = getUsers(userExpenses);
        List<ReportEntry> reportEntries = getSingleReportEntries(userExpenses, allUsers);
        List<ReportEntry> flattenedReportEntries = flattenSingleReportEntries(reportEntries);

        return createReport(expenseProject, flattenedReportEntries);
    }

    private ExpenseReport createReport(ExpenseProject expenseProject, List<ReportEntry> flattenedReportEntries) {
        ExpenseReport report = new ExpenseReport(expenseProject);
        for (ReportEntry entry : flattenedReportEntries) {
            int compareTo = entry.getAmount().compareTo(BigDecimal.ZERO);
            if (compareTo < 0) {
                report.addEntry(new ReportEntry(entry.getReceiver(), entry.getSender(), entry.getAmount().abs()));
            } else if (compareTo > 0) {
                report.addEntry(entry);
            }
        }
        return report;
    }

    private List<ReportEntry> flattenSingleReportEntries(List<ReportEntry> reportEntries) {
        List<ReportEntry> flattenedReportEntries = new ArrayList<>();
        List<ReportEntry> processedReportEntries = new ArrayList<>();
        for (ReportEntry entry : reportEntries) {
            if (!processedReportEntries.contains(entry)) {
                for (ReportEntry entrySearch : reportEntries) {
                    if (!processedReportEntries.contains(entrySearch) &&
                            entry.getSender().equals(entrySearch.getReceiver()) &&
                            entry.getReceiver().equals(entrySearch.getSender())
                            ) {
                        flattenedReportEntries.add(
                                new ReportEntry(entry.getSender(), entry.getReceiver(),
                                        entry.getAmount().subtract(entrySearch.getAmount())));
                        processedReportEntries.add(entrySearch);
                    }
                }
            }
        }
        return flattenedReportEntries;
    }

    private List<ReportEntry> getSingleReportEntries(List<UserExpense> userExpenses, List<User> allUsers) {
        return StreamSupport.stream(userExpenses).flatMap(e -> {
            BigDecimal amount = e.getAmount().
                    divide(new BigDecimal(allUsers.size()), DECIMAL_PLACES, RoundingMode.HALF_UP);
            return StreamSupport.stream(allUsers).filter(u -> !u.equals(e.getUser())).
                    map(u -> new ReportEntry(u, e.getUser(), amount));
        }).collect(Collectors.toList());
    }

    private List<User> getUsers(List<UserExpense> userExpenses) {
        return StreamSupport.stream(userExpenses).
                map(UserExpense::getUser).
                collect(Collectors.toList());
    }
}
