package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krzysiekz.costy.model.ReportEntry;

import java.util.List;

import javax.inject.Inject;

import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ReportPresenter;
import costy.krzysiekz.com.costy.view.ReportView;

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;


public class ReportFragment extends Fragment implements ReportView {


    public ReportPresenter presenter;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        String projectName = getArguments().getString(PROJECT_NAME);

        CostyApplication.component().inject(this);
        presenter.attachView(this);
        presenter.loadReportEntries(projectName);
        return view;
    }

    @Inject
    void setPresenter(ReportPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showReportEntries(List<ReportEntry> entries) {

    }
}
