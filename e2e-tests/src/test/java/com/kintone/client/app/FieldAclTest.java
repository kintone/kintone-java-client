package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.FieldAclBuilder;
import com.kintone.client.helper.Fields;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.FieldAccessibility;
import com.kintone.client.model.app.FieldRight;
import com.kintone.client.model.app.FieldRightEntity;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** AppClientのフィールドアクセス権設定に関するテスト */
public class FieldAclTest extends ApiTestBase {
    @Test
    public void getFieldAcl_getFieldAclPreview() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "getFieldAcl_getFieldAclPreview");
        app.addFields(text, number, userSelect);

        FieldAclBuilder builder = new FieldAclBuilder();
        builder.target(text).user(getDefaultUser(), true, true).everyone(false, false);
        builder.target(number).field(userSelect, true, false).everyone(true, true);
        app.updateFieldAcl(builder).deploy();
        long revision = app.getAppRevision(true);

        FieldRight r1 =
                right(
                        text,
                        entity(EntityType.USER, getDefaultUser(), FieldAccessibility.WRITE),
                        entity(EntityType.GROUP, "everyone", FieldAccessibility.NONE));
        FieldRight r2 =
                right(
                        number,
                        entity(EntityType.FIELD_ENTITY, userSelect.getCode(), FieldAccessibility.READ),
                        entity(EntityType.GROUP, "everyone", FieldAccessibility.WRITE));

        GetFieldAclRequest req1 = new GetFieldAclRequest();
        req1.setApp(app.id());
        GetFieldAclResponseBody resp1 = client.app().getFieldAcl(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.getRights()).containsExactly(r1, r2);

        builder = new FieldAclBuilder().target(text).everyone(true, false);
        app.updateFieldAcl(builder);
        FieldRight r3 = right(text, entity(EntityType.GROUP, "everyone", FieldAccessibility.READ));

        GetFieldAclPreviewRequest req2 = new GetFieldAclPreviewRequest();
        req2.setApp(app.id());
        GetFieldAclPreviewResponseBody resp2 = client.app().getFieldAclPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getRights()).containsExactly(r3);
    }

    @Test
    public void updateFieldAcl() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "updateFieldAcl");
        app.addFields(text, number, userSelect).deploy();
        long revision = app.getAppRevision(true);

        FieldRight r1 =
                right(
                        text,
                        entity(EntityType.USER, getDefaultUser(), FieldAccessibility.WRITE),
                        entity(EntityType.GROUP, "everyone", FieldAccessibility.NONE));
        FieldRight r2 =
                right(
                        number,
                        entity(EntityType.FIELD_ENTITY, userSelect.getCode(), FieldAccessibility.NONE),
                        entity(EntityType.GROUP, "everyone", FieldAccessibility.READ));

        UpdateFieldAclRequest req = new UpdateFieldAclRequest();
        req.setApp(app.id());
        req.setRevision(revision);
        req.setRights(Arrays.asList(r1, r2));
        UpdateFieldAclResponseBody resp = client.app().updateFieldAcl(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        List<FieldRight> previewRights = app.getFieldAcl(true);
        assertThat(previewRights).containsExactly(r1, r2);

        List<FieldRight> deployedRights = app.getFieldAcl(false);
        assertThat(deployedRights).isEmpty();
    }

    private FieldRight right(FieldProperty field, FieldRightEntity... entities) {
        return new FieldRight().setCode(field.getCode()).setEntities(Arrays.asList(entities));
    }

    private FieldRightEntity entity(EntityType type, String code, FieldAccessibility acl) {
        return new FieldRightEntity()
                .setEntity(new Entity(type, code))
                .setAccessibility(acl)
                .setIncludeSubs(false);
    }
}
