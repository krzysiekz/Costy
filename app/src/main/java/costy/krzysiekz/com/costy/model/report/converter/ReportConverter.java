package costy.krzysiekz.com.costy.model.report.converter;

import com.krzysiekz.costy.model.ExpenseReport;

public interface ReportConverter<R> {
    R convert(ExpenseReport report);
}
