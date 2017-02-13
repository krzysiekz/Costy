package costy.krzysiekz.com.costy.view.activity.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.ExpenseProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import costy.krzysiekz.com.costy.R;
import java8.util.function.Functions;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ProjectAdapter extends SelectableAdapter<ProjectAdapter.ViewHolder> {

    private List<ExpenseProject> items = new ArrayList<>();
    private ClickListener listener;

    public ProjectAdapter(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExpenseProject project = items.get(position);
        holder.projectNameTextView.setText(project.getName());

        holder.expenseSelectedOverlay.setBackgroundResource(isSelected(position) ? R.color.colorSelected : R.color.colorUnselected);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<ExpenseProject> getItems() {
        return items;
    }

    public void setProjects(List<ExpenseProject> projects) {
        this.items = new ArrayList<>(projects);
    }

    public Map<Integer, ExpenseProject> getSelectedProjects() {
        return StreamSupport.stream(getSelectedItems()).
                collect(Collectors.toMap(Functions.identity(), i -> items.get(i)));
    }

    private void removeItem(int position) {
        items.remove(position);
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

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            items.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {
        TextView projectNameTextView;
        View expenseSelectedOverlay;

        private ClickListener listener;

        ViewHolder(View view, ClickListener listener) {
            super(view);
            projectNameTextView = (TextView) view.findViewById(R.id.item_project_name);
            expenseSelectedOverlay = view.findViewById(R.id.item_project_selected_overlay);

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
