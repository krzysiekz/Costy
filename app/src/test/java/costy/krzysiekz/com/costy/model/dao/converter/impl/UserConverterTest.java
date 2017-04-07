package costy.krzysiekz.com.costy.model.dao.converter.impl;


import com.krzysiekz.costy.model.User;

import org.junit.Before;
import org.junit.Test;

import costy.krzysiekz.com.costy.model.dao.entity.UserEntity;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserConverterTest {

    private UserConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new UserConverter();
    }

    @Test
    public void shouldConvertBusinessObjectToEntity() {
        //given
        User user = new User("John");
        //when
        UserEntity userEntity = converter.toEntity(user);
        //then
        assertThat(userEntity.getName()).isEqualTo(user.getName());
    }

    @Test
    public void shouldConvertEntityToBusinessObject() {
        //given
        UserEntity entity = new UserEntity();
        entity.setName("Kate");
        //when
        User user = converter.fromEntity(entity);
        //then
        assertThat(user.getName()).isEqualTo(entity.getName());
    }

}