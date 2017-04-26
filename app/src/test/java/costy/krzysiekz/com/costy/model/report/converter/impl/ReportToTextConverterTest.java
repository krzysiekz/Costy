package costy.krzysiekz.com.costy.model.report.converter.impl;


import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ReportToTextConverterTest {

    @Test
    public void shouldConvertReport() {
        //given
        ReportToTextConverter reportToTextConverter = new ReportToTextConverter();
        ExpenseReport expenseReport = new ExpenseReport(new ExpenseProject("Sample name"));

        User kate = new User("Kate");
        User john = new User("John");

        expenseReport.addEntry(getReportEntry(kate, john, "10", "EUR"));
        expenseReport.addEntry(getReportEntry(john, kate, "15", "PLN"));
        //when
        String reportAsText = reportToTextConverter.convert(expenseReport);
        //then
        assertThat(reportAsText).isEqualTo("Kate -> John: 10 EUR\nJohn -> Kate: 15 PLN");
    }

    private ReportEntry getReportEntry(User from, User to, String amount, String currency) {
        return new ReportEntry.Builder().
                withSender(from).
                withReceiver(to).
                withAmount(new BigDecimal(amount)).
                withCurrency(new Currency(currency)).build();
    }
}