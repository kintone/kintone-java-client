package com.kintone.client;

import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import lombok.Getter;

@Getter
public class GroupSetting {
    private final String code;
    private final String name;

    public GroupSetting(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Entity toEntity() {
        return new Entity(EntityType.GROUP, this.code);
    }
}
