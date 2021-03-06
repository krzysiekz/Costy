package costy.krzysiekz.com.costy.presenter.impl;

import com.google.common.collect.ComparisonChain;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;
import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.service.ExpenseCalculator;

import java.util.Collections;
import java.util.List;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.model.report.converter.impl.ReportToTextConverter;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.ReportView;

public class ReportPresenter implements Presenter<ReportView> {

    private ProjectsRepository repository;
    private ExpenseCalculator expenseCalculator;
    private ReportView reportView;
    private ReportToTextConverter reportConverter;

    public ReportPresenter(ProjectsRepository repository, ExpenseCalculator expenseCalculator,
                           ReportToTextConverter reportConverter) {
        this.repository = repository;
        this.expenseCalculator = expenseCalculator;
        this.reportConverter = reportConverter;
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
        List<ReportEntry> reportEntries = report.getEntries();
        Collections.sort(reportEntries, (r1, r2) -> ComparisonChain.start().
                compare(r1.getSender().getName(), r2.getSender().getName()).
                compare(r1.getCurrency().getName(), r2.getCurrency().getName()).result());
        reportView.showReportEntries(reportEntries);
    }

    public void shareReport(String projectName) {
        ExpenseProject project = repository.getProject(projectName);
        ExpenseReport report = expenseCalculator.calculate(project);
        reportView.shareReport(projectName, reportConverter.convert(report));
    }
}
