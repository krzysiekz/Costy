package costy.krzysiekz.com.costy.model.report.converter.impl;

import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.UserExpense;

import java.text.MessageFormat;

import costy.krzysiekz.com.costy.model.report.converter.ReportConverter;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ReportToTextConverter implements ReportConverter<String> {

    private static final String REPORT_FORMAT = "Project: {0}\n\nExpenses:\n{1}\n\nReport:\n{2}";

    @Override
    public String convert(ExpenseReport report) {
        String projectName = report.getProject().getName();
        String expenses = StreamSupport.stream(report.getProject().getExpenses()).
                map(UserExpense::toString).
                collect(Collectors.joining("\n\n"));
        String reportEntries = StreamSupport.stream(report.getEntries()).
                map(ReportEntry::toString).
                collect(Collectors.joining("\n"));
        return MessageFormat.format(REPORT_FORMAT,
                projectName, expenses, reportEntries);
    }
}
