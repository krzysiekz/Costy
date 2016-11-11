package com.krzysiekz.costy.model;

import java.math.BigDecimal;
import java.util.List;

public class UserExpense {

    private List<User> receivers;
    private User user;
    private BigDecimal amount;

    public UserExpense(User user, BigDecimal amount, List<User> receivers) {
        this.user = user;
        this.amount = amount;
        this.receivers = receivers;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public List<User> getReceivers() {
        return receivers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserExpense that = (UserExpense) o;

        if (receivers != null ? !receivers.equals(that.receivers) : that.receivers != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;

    }

    @Override
    public int hashCode() {
        int result = receivers != null ? receivers.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserExpense{" +
                "receivers=" + receivers +
                ", user=" + user +
                ", amount=" + amount +
                '}';
    }
}
