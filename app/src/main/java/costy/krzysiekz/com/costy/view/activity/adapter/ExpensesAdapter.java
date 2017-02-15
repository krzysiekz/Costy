package costy.krzysiekz.com.costy.view.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import costy.krzysiekz.com.costy.R;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ExpensesAdapter extends SelectableAdapter<ExpensesAdapter.ViewHolder, UserExpense> {

    public ExpensesAdapter(ClickListener listener) {
        super(listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserExpense expense = items.get(position);
        holder.expenseDescription.setText(expense.getDescription());
        holder.expenseFrom.setText(expense.getUser().getName());
        String recipients = TextUtils.join(", ", StreamSupport.stream(expense.getReceivers()).
                map(User::getName).collect(Collectors.toList()));
        holder.expenseTo.setText(recipients);
        holder.expenseAmount.setText(expense.getAmount().toPlainString());

        holder.expenseSelectedOverlay.setBackgroundResource(isSelected(position) ? R.color.colorSelected : R.color.colorUnselected);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {
        TextView expenseDescription;
        TextView expenseFrom;
        TextView expenseTo;
        TextView expenseAmount;
        View expenseSelectedOverlay;

        private ClickListener listener;

        ViewHolder(View view, ClickListener listener) {
            super(view);
            expenseDescription = (TextView) view.findViewById(R.id.item_expense_description);
            expenseFrom = (TextView) view.findViewById(R.id.item_expense_from);
            expenseTo = (TextView) view.findViewById(R.id.item_expense_to);
            expenseAmount = (TextView) view.findViewById(R.id.item_expense_amount);
            expenseSelectedOverlay = view.findViewById(R.id.item_expense_selected_overlay);

            this.listener = listener;

            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            return listener != null && listener.onItemLongClicked(getAdapterPosition());

        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }
    }
}
