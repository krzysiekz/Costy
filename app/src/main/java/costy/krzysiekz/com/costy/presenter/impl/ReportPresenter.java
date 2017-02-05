package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.service.ExpenseCalculator;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.ReportView;

public class ReportPresenter implements Presenter<ReportView> {

    private ProjectsRepository repository;
    private ExpenseCalculator expenseCalculator;
    private ReportView reportView;

    public ReportPresenter(ProjectsRepository repository, ExpenseCalculator expenseCalculator) {
        this.repository = repository;
        this.expenseCalculator = expenseCalculator;
    }

    @Override
    public void attachView(ReportView view) {
        this.reportView = view;
    }

    @Override
    public void detachView() {

    }

    public void loadReportEntries(String projectName) {
        ExpenseProject project = repository.getProject(projectName);
        ExpenseReport report = expenseCalculator.calculate(project);
        reportView.showReportEntries(report.getEntries());
    }
}
