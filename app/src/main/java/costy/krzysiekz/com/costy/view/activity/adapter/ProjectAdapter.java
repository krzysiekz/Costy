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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExpenseProject project = items.get(position);
        holder.projectNameTextView.setText(project.getName());
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
            projectNameTextView = (TextView) view;
        }
    }
}
