package com.krzysiekz.costy.model;

import java.util.ArrayList;
import java.util.List;

public class ExpenseProject {
    private List<UserExpense> expenses;
    private String name;

    public ExpenseProject(String projectName) {
        this.expenses = new ArrayList<>();
        this.name = projectName;
    }

    public void addExpense(UserExpense userExpense) {
        expenses.add(userExpense);
    }

    public List<UserExpense> getExpenses() {
        return expenses;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpenseProject project = (ExpenseProject) o;

        if (expenses != null ? !expenses.equals(project.expenses) : project.expenses != null)
            return false;
        return name != null ? name.equals(project.name) : project.name == null;

    }

    @Override
    public int hashCode() {
        int result = expenses != null ? expenses.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExpenseProject{" +
                "expenses=" + expenses +
                ", name='" + name + '\'' +
                '}';
    }
}
