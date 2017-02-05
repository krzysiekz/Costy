package costy.krzysiekz.com.costy.view;

import com.krzysiekz.costy.model.ReportEntry;

import java.util.List;

public interface ReportView extends MVPView {
    void showReportEntries(List<ReportEntry> entries);
}
