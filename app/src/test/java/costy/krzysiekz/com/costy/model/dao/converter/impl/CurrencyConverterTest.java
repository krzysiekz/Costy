package costy.krzysiekz.com.costy.model.dao.converter.impl;

import com.krzysiekz.costy.model.Currency;

import org.junit.Before;
import org.junit.Test;

import costy.krzysiekz.com.costy.model.dao.entity.CurrencyEntity;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CurrencyConverterTest {

    private CurrencyConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new CurrencyConverter();
    }

    @Test
    public void shouldConvertBusinessObjectToEntity() {
        //given
        Currency currency = new Currency("PLN");
        //when
        CurrencyEntity currencyEntity = converter.toEntity(currency);
        //then
        assertThat(currencyEntity.getName()).isEqualTo(currency.getName());
    }

    @Test
    public void shouldConvertEntityToBusinessObject() {
        //given
        CurrencyEntity entity = new CurrencyEntity("EUR");
        //when
        Currency currency = converter.fromEntity(entity);
        //then
        assertThat(currency.getName()).isEqualTo(entity.getName());
    }
}