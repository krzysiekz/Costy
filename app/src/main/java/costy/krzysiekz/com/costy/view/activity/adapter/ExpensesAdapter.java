package costy.krzysiekz.com.costy.view.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.R;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private List<UserExpense> userExpenses = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserExpense expense = userExpenses.get(position);
        holder.expenseDescription.setText(expense.getDescription());
        holder.expenseFrom.setText(expense.getUser().getName());
        String recipients = TextUtils.join(", ", StreamSupport.stream(expense.getReceivers()).
                map(User::getName).collect(Collectors.toList()));
        holder.expenseTo.setText(recipients);
        holder.expenseAmount.setText(expense.getAmount().toPlainString());
    }

    @Override
    public int getItemCount() {
        return userExpenses.size();
    }

    public List<UserExpense> getItems() {
        return userExpenses;
    }

    public void setExpenses(List<UserExpense> expenses) {
        this.userExpenses = expenses;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView expenseDescription;
        TextView expenseFrom;
        TextView expenseTo;
        TextView expenseAmount;

        ViewHolder(View view) {
            super(view);
            expenseDescription = (TextView) view.findViewById(R.id.item_expense_description);
            expenseFrom = (TextView) view.findViewById(R.id.item_expense_from);
            expenseTo = (TextView) view.findViewById(R.id.item_expense_to);
            expenseAmount = (TextView) view.findViewById(R.id.item_expense_amount);
        }
    }
}
