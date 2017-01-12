package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;

public class PeopleFragment extends Fragment implements PeopleView {

    public PeoplePresenter presenter;

    @BindView(R.id.people_recycler_view)
    RecyclerView peopleRecyclerView;

    public PeopleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.bind(this, view);
        CostyApplication.component().inject(this);

        setupPeopleRecyclerView();

        presenter.attachView(this);
        presenter.loadProjectPeople(getArguments().getString(PROJECT_NAME));
        return view;
    }

    private void setupPeopleRecyclerView() {
        PeopleAdapter adapter = new PeopleAdapter();
        peopleRecyclerView.setAdapter(adapter);
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
}
