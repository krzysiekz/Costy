package com.krzysiekz.costy.service;


import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.ExpenseReport;

public interface ExpenseCalculator {
    ExpenseReport calculate(ExpenseProject expenseProject);
}
