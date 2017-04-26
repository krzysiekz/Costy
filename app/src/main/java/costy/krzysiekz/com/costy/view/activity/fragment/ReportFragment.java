package costy.krzysiekz.com.costy.view.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    @BindView(R.id.share_report_button)
    FloatingActionButton shareReportButton;

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

        shareReportButton.setOnClickListener(__ -> presenter.shareReport(projectName));
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

    @Override
    public void shareReport(String projectName, String reportAsText) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType(getString(R.string.share_content_type));
        txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, projectName);
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, reportAsText);
        startActivity(Intent.createChooser(txtIntent, getString(R.string.share_content_text)));
    }
}
