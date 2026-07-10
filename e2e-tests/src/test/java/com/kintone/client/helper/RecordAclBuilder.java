package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateRecordAclRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.*;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.ArrayList;
import java.util.List;

public class RecordAclBuilder {
    private final List<RecordRight> rights = new ArrayList<>();

    private RecordAclBuilder addRecordRightEntity(RecordRightEntity r) {
        rights.get(rights.size() - 1).getEntities().add(r);
        return this;
    }

    public RecordAclBuilder target(String query) {
        RecordRight right = new RecordRight();
        right.setFilterCond(query);
        right.setEntities(new ArrayList<>());
        rights.add(right);
        return this;
    }

    public RecordAclBuilder any() {
        return target("");
    }

    public RecordAclBuilder user(String code, boolean viewable, boolean editable, boolean deletable) {
        RecordRightEntity r =
                new RecordRightEntity()
                        .setEntity(new Entity(EntityType.USER, code))
                        .setViewable(viewable)
                        .setEditable(editable)
                        .setDeletable(deletable)
                        .setIncludeSubs(false);
        return addRecordRightEntity(r);
    }

    public RecordAclBuilder group(
            String code, boolean viewable, boolean editable, boolean deletable) {
        RecordRightEntity r =
                new RecordRightEntity()
                        .setEntity(new Entity(EntityType.GROUP, code))
                        .setViewable(viewable)
                        .setEditable(editable)
                        .setDeletable(deletable)
                        .setIncludeSubs(false);
        return addRecordRightEntity(r);
    }

    public RecordAclBuilder everyone(boolean viewable, boolean editable, boolean deletable) {
        return group("everyone", viewable, editable, deletable);
    }

    public RecordAclBuilder org(
            String code, boolean includeSubs, boolean viewable, boolean editable, boolean deletable) {
        RecordRightEntity r =
                new RecordRightEntity()
                        .setEntity(new Entity(EntityType.ORGANIZATION, code))
                        .setViewable(viewable)
                        .setEditable(editable)
                        .setDeletable(deletable)
                        .setIncludeSubs(includeSubs);
        return addRecordRightEntity(r);
    }

    public RecordAclBuilder field(
            String code, boolean viewable, boolean editable, boolean deletable) {
        RecordRightEntity r =
                new RecordRightEntity()
                        .setEntity(new Entity(EntityType.FIELD_ENTITY, code))
                        .setViewable(viewable)
                        .setEditable(editable)
                        .setDeletable(deletable)
                        .setIncludeSubs(false);
        return addRecordRightEntity(r);
    }

    public RecordAclBuilder field(
            FieldProperty field, boolean viewable, boolean editable, boolean deletable) {
        return field(field.getCode(), viewable, editable, deletable);
    }

    UpdateRecordAclRequest build(long appId) {
        UpdateRecordAclRequest req = new UpdateRecordAclRequest();
        req.setApp(appId);
        req.setRights(rights);
        return req;
    }
}
