package costy.krzysiekz.com.costy.view.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.User;

import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.R;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private List<User> people = new ArrayList<>();

    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PeopleAdapter.ViewHolder holder, int position) {
        User user = people.get(position);
        holder.personNameTextView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public List<User> getPeople() {
        return people;
    }

    public void setPeople(List<User> people) {
        this.people = people;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView personNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            personNameTextView = (TextView) itemView.findViewById(R.id.item_person_name);
        }
    }
}
