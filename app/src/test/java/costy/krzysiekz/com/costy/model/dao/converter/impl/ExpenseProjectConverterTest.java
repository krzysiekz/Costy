package costy.krzysiekz.com.costy.model.dao.converter.impl;


import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import org.junit.Before;
import org.junit.Test;

import costy.krzysiekz.com.costy.model.dao.entity.CurrencyEntity;
import costy.krzysiekz.com.costy.model.dao.entity.ExpenseProjectEntity;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ExpenseProjectConverterTest {

    private ExpenseProjectConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new ExpenseProjectConverter();
    }

    @Test
    public void shouldConvertBusinessObjectToEntity() {
        //given
        ExpenseProject project = new ExpenseProject("Sample project", new Currency("PLN"));
        //when
        ExpenseProjectEntity entity = converter.toEntity(project);
        //then
        assertThat(entity.getName()).isEqualTo(project.getName());
    }

    @Test
    public void shouldConvertEntityToBusinessObject() {
        //given
        ExpenseProjectEntity entity = new ExpenseProjectEntity();
        entity.setName("Project 2");
        entity.setDefaultCurrency(new CurrencyEntity("EUR"));
        //when
        ExpenseProject project = converter.fromEntity(entity);
        //then
        assertThat(project.getName()).isEqualTo(entity.getName());
    }
}