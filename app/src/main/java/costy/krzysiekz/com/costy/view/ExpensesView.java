package costy.krzysiekz.com.costy.view;

import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.List;
import java.util.Set;

public interface ExpensesView extends MVPView {
    void showExpenses(List<UserExpense> expenses);
    void showAddExpenseDialog(List<User> people);
    void removeExpenses(Set<Integer> expensesPositions);
}
