package costy.krzysiekz.com.costy.view.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krzysiekz.costy.model.User;

import costy.krzysiekz.com.costy.R;

public class PeopleAdapter extends SelectableAdapter<PeopleAdapter.ViewHolder, User> {

    public PeopleAdapter(ClickListener listener) {
        super(listener);
    }

    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(PeopleAdapter.ViewHolder holder, int position) {
        User user = items.get(position);
        holder.personNameTextView.setText(user.getName());

        holder.expenseSelectedOverlay.setBackgroundResource(isSelected(position)
                ? R.color.colorSelected : R.color.colorUnselected);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {
        TextView personNameTextView;
        View expenseSelectedOverlay;

        private ClickListener listener;

        ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            personNameTextView = (TextView) itemView.findViewById(R.id.item_person_name);
            expenseSelectedOverlay = itemView.findViewById(R.id.item_person_selected_overlay);

            this.listener = listener;

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return listener != null && listener.onItemLongClicked(getAdapterPosition());
        }
    }
}
