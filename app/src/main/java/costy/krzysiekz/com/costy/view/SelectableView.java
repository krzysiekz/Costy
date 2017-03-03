package costy.krzysiekz.com.costy.view;


import android.support.v7.view.ActionMode;

import java.util.Map;

import costy.krzysiekz.com.costy.view.activity.adapter.SelectableAdapter;

public interface SelectableView<T> {
    SelectableAdapter<?, T> getAdapter();

    void handleRemoveButtonClicked(Map<Integer, T> selectedItemObjects);

    void setActionMode(ActionMode actionMode);

    ActionMode getActionMode();

    void handleSingleItemClick(T clickedItem);

    ActionMode startSupportActionMode();
}
