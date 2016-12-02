package costy.krzysiekz.com.costy.view.activity.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.ExpenseProject;

import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.R;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private List<ExpenseProject> items = new ArrayList<>();
    private ProjectAdapterListener listener;

    public ProjectAdapter(ProjectAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExpenseProject project = items.get(position);
        holder.projectNameTextView.setText(project.getName());
        holder.itemView.setOnClickListener(__ -> listener.onProjectSelected(project));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<ExpenseProject> getItems() {
        return items;
    }

    public void setProjects(List<ExpenseProject> projects) {
        this.items = projects;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView projectNameTextView;

        ViewHolder(View view) {
            super(view);
            projectNameTextView = (TextView) view.findViewById(R.id.item_project_name);
        }
    }
}
