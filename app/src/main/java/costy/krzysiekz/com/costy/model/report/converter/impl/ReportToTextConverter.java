package costy.krzysiekz.com.costy.model.report.converter.impl;

import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;

import costy.krzysiekz.com.costy.model.report.converter.ReportConverter;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ReportToTextConverter implements ReportConverter<String> {

    @Override
    public String convert(ExpenseReport report) {
        return StreamSupport.stream(report.getEntries()).
                map(ReportEntry::toString).
                collect(Collectors.joining("\n"));
    }
}
