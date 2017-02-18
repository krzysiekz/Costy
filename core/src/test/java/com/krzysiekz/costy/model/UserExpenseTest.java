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
        UserExpense expense =
                new UserExpense(john, new BigDecimal("10"), Arrays.asList(john, kate), "Description");
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
        UserExpense expense =
                new UserExpense(john, new BigDecimal("10"), Collections.singletonList(john), "Description");
        //when
        boolean kateResult = expense.isUserUsed(kate);
        //then
        assertThat(kateResult).isFalse();
    }
}