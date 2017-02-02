package costy.krzysiekz.com.costy.view.activity.dialog;

import com.krzysiekz.costy.model.UserExpense;

public interface AddExpenseDialogListener {
    void onExpenseConfirmed(UserExpense expense);
}
