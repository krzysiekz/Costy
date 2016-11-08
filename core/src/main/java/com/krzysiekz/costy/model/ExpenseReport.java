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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpenseReport report = (ExpenseReport) o;

        if (entries != null ? !entries.equals(report.entries) : report.entries != null)
            return false;
        return project != null ? project.equals(report.project) : report.project == null;

    }

    @Override
    public int hashCode() {
        int result = entries != null ? entries.hashCode() : 0;
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExpenseReport{" +
                "entries=" + entries +
                ", project=" + project +
                '}';
    }
}
