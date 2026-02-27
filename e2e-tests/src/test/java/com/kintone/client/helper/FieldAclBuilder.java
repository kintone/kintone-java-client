package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateFieldAclRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.FieldAccessibility;
import com.kintone.client.model.app.FieldRight;
import com.kintone.client.model.app.FieldRightEntity;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.ArrayList;
import java.util.List;

public class FieldAclBuilder {
    private final List<FieldRight> rights = new ArrayList<>();

    private FieldAccessibility toAcl(boolean read, boolean write) {
        if (read && write) {
            return FieldAccessibility.WRITE;
        }
        if (read && !write) {
            return FieldAccessibility.READ;
        }
        if (!read && !write) {
            return FieldAccessibility.NONE;
        }
        throw new AssertionError("invalid field right.");
    }

    private FieldAclBuilder addFieldRightEntity(FieldRightEntity r) {
        rights.get(rights.size() - 1).getEntities().add(r);
        return this;
    }

    public FieldAclBuilder target(String field) {
        FieldRight right = new FieldRight();
        right.setCode(field);
        right.setEntities(new ArrayList<>());
        rights.add(right);
        return this;
    }

    public FieldAclBuilder target(FieldProperty field) {
        return target(field.getCode());
    }

    public FieldAclBuilder user(String code, boolean read, boolean write) {
        FieldRightEntity r = new FieldRightEntity();
        r.setEntity(new Entity(EntityType.USER, code));
        r.setIncludeSubs(false);
        r.setAccessibility(toAcl(read, write));
        return addFieldRightEntity(r);
    }

    public FieldAclBuilder group(String code, boolean read, boolean write) {
        FieldRightEntity r = new FieldRightEntity();
        r.setEntity(new Entity(EntityType.GROUP, code));
        r.setIncludeSubs(false);
        r.setAccessibility(toAcl(read, write));
        return addFieldRightEntity(r);
    }

    public FieldAclBuilder everyone(boolean read, boolean write) {
        return group("everyone", read, write);
    }

    public FieldAclBuilder org(String code, boolean includeSubs, boolean read, boolean write) {
        FieldRightEntity r = new FieldRightEntity();
        r.setEntity(new Entity(EntityType.ORGANIZATION, code));
        r.setIncludeSubs(includeSubs);
        r.setAccessibility(toAcl(read, write));
        return addFieldRightEntity(r);
    }

    public FieldAclBuilder field(String code, boolean read, boolean write) {
        FieldRightEntity r = new FieldRightEntity();
        r.setEntity(new Entity(EntityType.FIELD_ENTITY, code));
        r.setIncludeSubs(false);
        r.setAccessibility(toAcl(read, write));
        return addFieldRightEntity(r);
    }

    public FieldAclBuilder field(FieldProperty field, boolean read, boolean write) {
        return field(field.getCode(), read, write);
    }

    UpdateFieldAclRequest build(long appId) {
        UpdateFieldAclRequest req = new UpdateFieldAclRequest();
        req.setApp(appId);
        req.setRights(rights);
        return req;
    }
}
