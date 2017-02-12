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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import costy.krzysiekz.com.costy.R;
import java8.util.function.Functions;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ExpensesAdapter extends SelectableAdapter<ExpensesAdapter.ViewHolder> {

    private List<UserExpense> userExpenses = new ArrayList<>();
    private ClickListener listener;

    public ExpensesAdapter(ClickListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);

        return new ViewHolder(itemView, listener);
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

        holder.expenseSelectedOverlay.setBackgroundResource(isSelected(position) ? R.color.colorSelected : R.color.colorUnselected);
    }

    @Override
    public int getItemCount() {
        return userExpenses.size();
    }

    public List<UserExpense> getItems() {
        return userExpenses;
    }

    public void setExpenses(List<UserExpense> expenses) {
        this.userExpenses = new ArrayList<>(expenses);
    }

    private void removeItem(int position) {
        userExpenses.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, (lhs, rhs) -> rhs - lhs);

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }


    public Map<Integer, UserExpense> getSelectedExpenses() {
        List<Integer> selectedItems = getSelectedItems();
        return StreamSupport.stream(selectedItems).
                collect(Collectors.toMap(Functions.identity(), i -> userExpenses.get(i)));
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            userExpenses.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
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
