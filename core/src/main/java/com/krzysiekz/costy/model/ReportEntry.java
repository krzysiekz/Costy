package com.krzysiekz.costy.model;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class ReportEntry {
    private User sender;
    private User receiver;
    private BigDecimal amount;

    public ReportEntry(User sender, User receiver, BigDecimal amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportEntry entry = (ReportEntry) o;

        if (sender != null ? !sender.equals(entry.sender) : entry.sender != null) return false;
        if (receiver != null ? !receiver.equals(entry.receiver) : entry.receiver != null)
            return false;
        return amount != null ? amount.equals(entry.amount) : entry.amount == null;

    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} -> {1}: {2}", sender, receiver, amount);
    }
}
