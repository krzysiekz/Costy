package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krzysiekz.costy.model.ReportEntry;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ReportPresenter;
import costy.krzysiekz.com.costy.view.ReportView;
import costy.krzysiekz.com.costy.view.activity.adapter.ReportEntryAdapter;

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;


public class ReportFragment extends Fragment implements ReportView {


    public ReportPresenter presenter;

    @BindView(R.id.report_entries_recycler_view)
    public RecyclerView reportEntriesRecyclerView;

    public ReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        String projectName = getArguments().getString(PROJECT_NAME);

        ButterKnife.bind(this, view);
        CostyApplication.component().inject(this);

        setupReportEntriesRecyclerView();

        presenter.attachView(this);
        presenter.loadReportEntries(projectName);
        return view;
    }

    private void setupReportEntriesRecyclerView() {
        ReportEntryAdapter adapter = new ReportEntryAdapter();
        reportEntriesRecyclerView.setAdapter(adapter);
        reportEntriesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reportEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Inject
    void setPresenter(ReportPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showReportEntries(List<ReportEntry> entries) {
        ReportEntryAdapter adapter = (ReportEntryAdapter) reportEntriesRecyclerView.getAdapter();
        adapter.setReportEntries(entries);
        adapter.notifyDataSetChanged();
    }
}
