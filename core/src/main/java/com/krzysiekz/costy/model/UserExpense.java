package com.krzysiekz.costy.model;

import java.math.BigDecimal;

public class UserExpense {

    private User user;
    private BigDecimal amount;

    public UserExpense(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }
}
