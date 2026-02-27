package com.kintone.client;

import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import lombok.Getter;

@Getter
public class UserSetting {
    private final String code;
    private final String name;
    private final String password;

    public UserSetting(String code, String name, String password) {
        this.code = code;
        this.name = name;
        this.password = password;
    }

    public Entity toEntity() {
        return new Entity(EntityType.USER, this.code);
    }
}
