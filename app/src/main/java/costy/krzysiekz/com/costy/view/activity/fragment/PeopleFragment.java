package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.krzysiekz.costy.model.User;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.PeoplePresenter;
import costy.krzysiekz.com.costy.view.PeopleView;
import costy.krzysiekz.com.costy.view.activity.adapter.PeopleAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddPersonDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddPersonDialogListener;

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;

public class PeopleFragment extends Fragment implements PeopleView, AddPersonDialogListener {

    public PeoplePresenter presenter;

    @BindView(R.id.people_recycler_view)
    RecyclerView peopleRecyclerView;

    @BindView(R.id.add_person_button)
    FloatingActionButton addPersonButton;
    private String projectName;

    public PeopleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.bind(this, view);
        CostyApplication.component().inject(this);

        setupPeopleRecyclerView();

        this.projectName = getArguments().getString(PROJECT_NAME);
        presenter.attachView(this);
        presenter.loadProjectPeople(projectName);

        addPersonButton.setOnClickListener(__ -> showAddPersonDialog());
        return view;
    }

    private void setupPeopleRecyclerView() {
        PeopleAdapter adapter = new PeopleAdapter();
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
        adapter.setPeople(users);
        adapter.notifyDataSetChanged();
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
}
