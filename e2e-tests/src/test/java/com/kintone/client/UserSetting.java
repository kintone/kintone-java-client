package com.kintone.client;

import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import lombok.Getter;

@Getter
public class UserSetting {
    private final String code;
    private final String password;
    private final String name;

    public UserSetting(String code, String password, String name) {
        this.code = code;
        this.password = password;
        this.name = name;
    }

    public Entity toEntity() {
        return new Entity(EntityType.USER, this.code);
    }
}
