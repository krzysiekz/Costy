package costy.krzysiekz.com.costy.model.report.converter.impl;


import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ReportToTextConverterTest {

    @Test
    public void shouldConvertReport() {
        //given
        ReportToTextConverter reportToTextConverter = new ReportToTextConverter();
        ExpenseProject expenseProject = new ExpenseProject("Sample name");
        ExpenseReport expenseReport = new ExpenseReport(expenseProject);

        User kate = new User("Kate");
        User john = new User("John");

        expenseProject.addExpense(getExpense(kate, 30, "PLN", "Kate expense", Arrays.asList(kate, john)));
        expenseProject.addExpense(getExpense(john, 20, "EUR", "John expense", Arrays.asList(kate, john)));

        expenseReport.addEntry(getReportEntry(kate, john, "10", "EUR"));
        expenseReport.addEntry(getReportEntry(john, kate, "15", "PLN"));
        //when
        String reportAsText = reportToTextConverter.convert(expenseReport);
        //then
        assertThat(reportAsText).isEqualTo("Project: Sample name\n" +
                "\n" +
                "Expenses:\n" +
                "Kate expense: 30 PLN\n" +
                "Kate -> [Kate, John]\n" +
                "\n" +
                "John expense: 20 EUR\n" +
                "John -> [Kate, John]\n" +
                "\n" +
                "Report:\n" +
                "Kate -> John: 10 EUR\n" +
                "John -> Kate: 15 PLN");
    }

    private UserExpense getExpense(User kate, int amount, String currency, String description, List<User> receivers) {
        return new UserExpense.UserExpenseBuilder().
                withUser(kate).
                withAmount(new BigDecimal(amount)).
                withCurrency(new Currency(currency)).
                withDescription(description).
                withReceivers(receivers).build();
    }

    private ReportEntry getReportEntry(User from, User to, String amount, String currency) {
        return new ReportEntry.Builder().
                withSender(from).
                withReceiver(to).
                withAmount(new BigDecimal(amount)).
                withCurrency(new Currency(currency)).build();
    }
}