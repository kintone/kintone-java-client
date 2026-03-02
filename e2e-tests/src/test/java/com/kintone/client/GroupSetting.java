package com.kintone.client;

import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import lombok.Getter;

@Getter
public class GroupSetting {
    private final String code;

    public GroupSetting(String code) {
        this.code = code;
    }

    public Entity toEntity() {
        return new Entity(EntityType.GROUP, this.code);
    }
}
