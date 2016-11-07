package com.krzysiekz.costy.model;

import java.util.ArrayList;
import java.util.List;

public class ExpenseReport {

    private List<ReportEntry> entries;
    private ExpenseProject project;

    public ExpenseReport(ExpenseProject expenseProject) {
        entries = new ArrayList<>();
        this.project = expenseProject;
    }

    public List<ReportEntry> getEntries() {
        return entries;
    }

    public ExpenseProject getProject() {
        return project;
    }

    public void addEntry(ReportEntry entry) {
        this.entries.add(entry);
    }
}
