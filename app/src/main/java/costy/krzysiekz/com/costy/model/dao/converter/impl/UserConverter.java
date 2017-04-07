package costy.krzysiekz.com.costy.model.dao.converter.impl;

import com.krzysiekz.costy.model.User;

import costy.krzysiekz.com.costy.model.dao.converter.Converter;
import costy.krzysiekz.com.costy.model.dao.entity.UserEntity;

public class UserConverter implements Converter<UserEntity, User> {

    @Override
    public UserEntity toEntity(User businessObject) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(businessObject.getName());
        return userEntity;
    }

    @Override
    public User fromEntity(UserEntity entity) {
        return new User(entity.getName());
    }
}
