package com.krzysiekz.costy.model;

import java.math.BigDecimal;

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
}
