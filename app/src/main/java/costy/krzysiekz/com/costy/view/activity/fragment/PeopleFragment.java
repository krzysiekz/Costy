package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.krzysiekz.costy.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.PeoplePresenter;
import costy.krzysiekz.com.costy.view.PeopleView;
import costy.krzysiekz.com.costy.view.SelectableView;
import costy.krzysiekz.com.costy.view.activity.SelectableViewHandler;
import costy.krzysiekz.com.costy.view.activity.adapter.ClickListener;
import costy.krzysiekz.com.costy.view.activity.adapter.PeopleAdapter;
import costy.krzysiekz.com.costy.view.activity.adapter.SelectableAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddPersonDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddPersonDialogListener;

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;

public class PeopleFragment extends Fragment
        implements PeopleView, AddPersonDialogListener, ClickListener, SelectableView<User> {

    ActionMode actionMode;
    public PeoplePresenter presenter;

    @BindView(R.id.people_recycler_view)
    RecyclerView peopleRecyclerView;

    @BindView(R.id.add_person_button)
    FloatingActionButton addPersonButton;
    private String projectName;

    SelectableViewHandler<User> selectableViewHandler;

    public PeopleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.bind(this, view);
        CostyApplication.component().inject(this);

        setupPeopleRecyclerView();
        selectableViewHandler = new SelectableViewHandler<>(this);

        this.projectName = getArguments().getString(PROJECT_NAME);
        presenter.attachView(this);
        presenter.loadProjectPeople(projectName);

        addPersonButton.setOnClickListener(__ -> showAddPersonDialog());

        if (savedInstanceState != null) {
            AddPersonDialogFragment dialogFragment = (AddPersonDialogFragment) getFragmentManager().
                    findFragmentByTag(AddPersonDialogFragment.TAG);
            if (dialogFragment != null) {
                dialogFragment.setListener(this);
            }
        }

        return view;
    }

    private void setupPeopleRecyclerView() {
        PeopleAdapter adapter = new PeopleAdapter(this);
        peopleRecyclerView.setAdapter(adapter);
        peopleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Inject
    void setPresenter(PeoplePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPeople(List<User> users) {
        PeopleAdapter adapter = (PeopleAdapter) peopleRecyclerView.getAdapter();
        adapter.setItems(users);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removePeople(Set<Integer> positions) {
        PeopleAdapter adapter = (PeopleAdapter) peopleRecyclerView.getAdapter();
        adapter.removeItems(new ArrayList<>(positions));
        actionMode.finish();
    }

    @Override
    public void showPeopleUsedInExpensesError() {
        Toast.makeText(this.getContext(), R.string.cannot_delete_users_error,
                Toast.LENGTH_SHORT).show();
    }

    private void showAddPersonDialog() {
        AddPersonDialogFragment dialogFragment = new AddPersonDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.show(getFragmentManager(), AddPersonDialogFragment.TAG);
    }

    @Override
    public void onPersonNameConfirmed(String personName) {
        presenter.addPerson(projectName, personName);
    }

    @Override
    public void showWrongNameError() {
        Toast.makeText(this.getContext(), R.string.wrong_person_name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(int position) {
        selectableViewHandler.onItemClicked(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return selectableViewHandler.onItemLongClicked(position);
    }

    @Override
    public SelectableAdapter<?, User> getAdapter() {
        return (PeopleAdapter) peopleRecyclerView.getAdapter();
    }

    @Override
    public void handleRemoveButtonClicked(Map<Integer, User> selectedItemObjects) {
        presenter.removeUsers(projectName, selectedItemObjects);
    }

    @Override
    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    @Override
    public ActionMode getActionMode() {
        return actionMode;
    }

    @Override
    public void handleSingleItemClick(User clickedItem) {
    }

    @Override
    public ActionMode startSupportActionMode() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        return activity.startSupportActionMode(selectableViewHandler);
    }
}
