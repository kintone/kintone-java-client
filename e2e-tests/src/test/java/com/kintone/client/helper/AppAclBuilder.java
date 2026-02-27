package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateAppAclRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.AppRightEntity;
import java.util.ArrayList;
import java.util.List;

public class AppAclBuilder {
    private final List<AppRightEntity> rights = new ArrayList<>();

    private AppRightEntity getCurrent() {
        return rights.get(rights.size() - 1);
    }

    public AppAclBuilder user(String code) {
        AppRightEntity r = new AppRightEntity().setEntity(new Entity(EntityType.USER, code));
        r.setIncludeSubs(false);
        rights.add(r);
        return applyDefault();
    }

    public AppAclBuilder group(String code) {
        AppRightEntity r = new AppRightEntity().setEntity(new Entity(EntityType.GROUP, code));
        r.setIncludeSubs(false);
        rights.add(r);
        return applyDefault();
    }

    public AppAclBuilder org(String code, boolean incluseSubs) {
        AppRightEntity r = new AppRightEntity().setEntity(new Entity(EntityType.GROUP, code));
        r.setIncludeSubs(incluseSubs);
        rights.add(r);
        return applyDefault();
    }

    public AppAclBuilder creator() {
        AppRightEntity r = new AppRightEntity().setEntity(new Entity(EntityType.CREATOR, null));
        r.setIncludeSubs(false);
        rights.add(r);
        return all(true); // 作成者はデフォルトで全権限有り
    }

    public AppAclBuilder everyone() {
        return group("everyone");
    }

    public AppAclBuilder recordViewable(boolean viewable) {
        getCurrent().setRecordViewable(viewable);
        return this;
    }

    public AppAclBuilder recordAddable(boolean addable) {
        getCurrent().setRecordAddable(addable);
        return this;
    }

    public AppAclBuilder recordEditable(boolean editable) {
        getCurrent().setRecordEditable(editable);
        return this;
    }

    public AppAclBuilder recordDeletable(boolean deletable) {
        getCurrent().setRecordDeletable(deletable);
        return this;
    }

    public AppAclBuilder appEditable(boolean editable) {
        getCurrent().setAppEditable(editable);
        return this;
    }

    public AppAclBuilder recordImportable(boolean importable) {
        getCurrent().setRecordImportable(importable);
        return this;
    }

    public AppAclBuilder recordExportable(boolean exportable) {
        getCurrent().setRecordExportable(exportable);
        return this;
    }

    public AppAclBuilder all(boolean enable) {
        AppRightEntity current = getCurrent();
        current.setRecordViewable(enable);
        current.setRecordAddable(enable);
        current.setRecordEditable(enable);
        current.setRecordDeletable(enable);
        current.setAppEditable(enable);
        current.setRecordImportable(enable);
        current.setRecordExportable(enable);
        return this;
    }

    public AppAclBuilder applyDefault() {
        AppRightEntity current = getCurrent();
        current.setRecordViewable(true);
        current.setRecordAddable(true);
        current.setRecordEditable(true);
        current.setRecordDeletable(true);
        current.setAppEditable(true);
        current.setRecordImportable(false);
        current.setRecordExportable(false);
        return this;
    }

    UpdateAppAclRequest build(long appId) {
        UpdateAppAclRequest req = new UpdateAppAclRequest();
        req.setApp(appId);
        req.setRights(rights);
        return req;
    }
}
