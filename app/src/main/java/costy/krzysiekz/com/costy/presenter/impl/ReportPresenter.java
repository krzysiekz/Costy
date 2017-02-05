package costy.krzysiekz.com.costy.presenter.impl;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.ReportView;

public class ReportPresenter implements Presenter<ReportView> {


    public ReportPresenter(ProjectsRepository repository) {

    }

    @Override
    public void attachView(ReportView view) {

    }

    @Override
    public void detachView() {

    }

    public void loadReportEntries(String projectName) {

    }
}
