package com.kintone.client;

import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import lombok.Getter;

@Getter
public class OrgSetting {
    private final String code;

    public OrgSetting(String code) {
        this.code = code;
    }

    public Entity toEntity() {
        return new Entity(EntityType.ORGANIZATION, this.code);
    }
}
