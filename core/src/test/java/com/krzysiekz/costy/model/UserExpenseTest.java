package com.krzysiekz.costy.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserExpenseTest {

    @Test
    public void shouldReturnTrueIfUserUsedInExpense() {
        //given
        User john = new User("John");
        User kate = new User("Kate");
        UserExpense expense = new UserExpense.UserExpenseBuilder().
                withUser(john).withAmount(new BigDecimal("10")).
                withReceivers(Arrays.asList(john, kate)).withDescription("Description").build();
        //when
        boolean johnResult = expense.isUserUsed(john);
        boolean kateResult = expense.isUserUsed(kate);
        //then
        assertThat(johnResult).isTrue();
        assertThat(kateResult).isTrue();
    }

    @Test
    public void shouldReturnFalseIfUserNotUsedInExpense() {
        //given
        User john = new User("John");
        User kate = new User("Kate");
        UserExpense expense = new UserExpense.UserExpenseBuilder().
                withUser(john).withAmount(new BigDecimal("10")).
                withReceivers(Collections.singletonList(john)).withDescription("Description").build();
        //when
        boolean kateResult = expense.isUserUsed(kate);
        //then
        assertThat(kateResult).isFalse();
    }
}