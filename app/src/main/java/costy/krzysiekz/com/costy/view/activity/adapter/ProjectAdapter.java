package costy.krzysiekz.com.costy.view.activity.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.ExpenseProject;

import costy.krzysiekz.com.costy.R;

public class ProjectAdapter extends SelectableAdapter<ProjectAdapter.ViewHolder, ExpenseProject> {

    public ProjectAdapter(ClickListener listener) {
        super(listener);
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
