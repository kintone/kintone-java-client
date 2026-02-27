package com.kintone.client;

import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import lombok.Getter;

@Getter
public class OrgSetting {
    private final String code;
    private final String name;
    private final String parent;

    public OrgSetting(String code, String name, String parent) {
        this.code = code;
        this.name = name;
        this.parent = parent;
    }

    public Entity toEntity() {
        return new Entity(EntityType.ORGANIZATION, this.code);
    }
}
