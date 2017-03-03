package costy.krzysiekz.com.costy.view.activity;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Set;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.view.SelectableView;
import costy.krzysiekz.com.costy.view.activity.adapter.SelectableAdapter;

public class SelectableViewHandler<T> implements ActionMode.Callback {

    private SelectableView<T> selectableView;

    public SelectableViewHandler(SelectableView<T> selectableView) {
        this.selectableView = selectableView;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        SelectableAdapter<?, T> adapter = selectableView.getAdapter();
        switch (item.getItemId()) {
            case R.id.menu_remove:
                selectableView.handleRemoveButtonClicked(adapter.getSelectedItemObjects());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        SelectableAdapter<?, T> adapter = selectableView.getAdapter();
        adapter.clearSelection();
        selectableView.setActionMode(null);
    }

    public void onItemClicked(int position) {
        if (selectableView.getActionMode() != null) {
            toggleSelection(position);
        } else {
            SelectableAdapter<?, T> adapter = selectableView.getAdapter();
            selectableView.handleSingleItemClick(adapter.getItems().get(position));
        }
    }

    public boolean onItemLongClicked(int position) {
        if (selectableView.getActionMode() == null) {
            selectableView.setActionMode(selectableView.startSupportActionMode());
        }

        toggleSelection(position);
        return true;
    }

    private void toggleSelection(int position) {
        SelectableAdapter<?, T> adapter = selectableView.getAdapter();
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            selectableView.getActionMode().finish();
        } else {
            selectableView.getActionMode().setTitle(String.valueOf(count));
            selectableView.getActionMode().invalidate();
        }
    }

    public void removeItems(Set<Integer> positions) {
        SelectableAdapter<?, T> adapter = selectableView.getAdapter();
        adapter.removeItems(new ArrayList<>(positions));
        selectableView.getActionMode().finish();
    }


}
