package costy.krzysiekz.com.costy.view;

import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.List;

public interface ExpensesView extends MVPView {
    void showExpenses(List<UserExpense> expenses);
    void showAddExpenseDialog(List<User> people);
}
