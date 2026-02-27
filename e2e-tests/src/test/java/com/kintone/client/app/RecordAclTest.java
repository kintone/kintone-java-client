package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.FieldAclBuilder;
import com.kintone.client.helper.Fields;
import com.kintone.client.helper.RecordAclBuilder;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.*;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** AppClientのレコードアクセス権設定に関するテスト */
public class RecordAclTest extends ApiTestBase {
    @Test
    public void evaluateRecordAcl() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "evaluateRecordAcl");
        app.addFields(text, number, userSelect);

        RecordAclBuilder recordAcl = new RecordAclBuilder();
        recordAcl
                .target("$id >= 2")
                .user(getDefaultUser(), true, true, false)
                .everyone(false, false, false);
        app.updateRecordAcl(recordAcl);

        FieldAclBuilder fieldAcl = new FieldAclBuilder();
        fieldAcl.target(text).user(getDefaultUser(), true, true).everyone(false, false);
        fieldAcl.target(number).field(userSelect, true, true).everyone(true, false);
        app.updateFieldAcl(fieldAcl).deploy();

        long recordId1 = app.addRecord();
        long recordId2 = app.addRecord();

        EvaluateRecordAclRequest req = new EvaluateRecordAclRequest();
        req.setApp(app.id());
        req.setIds(Arrays.asList(recordId1, recordId2));
        EvaluateRecordAclResponseBody resp = client.app().evaluateRecordAcl(req);
        assertThat(resp.getRights()).hasSize(2);

        EvaluatedRecordRight r1 = resp.getRights().get(0);
        assertThat(r1.getId()).isEqualTo(recordId1);
        assertThat(r1.getRecord()).isEqualTo(new EvaluatedRecordRightEntity(true, true, true));
        assertThat(r1.getFields()).hasSize(3);
        assertThat(r1.getFields())
                .containsEntry(text.getCode(), new EvaluatedFieldRightEntity(true, true));
        assertThat(r1.getFields())
                .containsEntry(number.getCode(), new EvaluatedFieldRightEntity(true, false));
        assertThat(r1.getFields())
                .containsEntry(userSelect.getCode(), new EvaluatedFieldRightEntity(true, true));

        EvaluatedRecordRight r2 = resp.getRights().get(1);
        assertThat(r2.getId()).isEqualTo(recordId2);
        assertThat(r2.getRecord()).isEqualTo(new EvaluatedRecordRightEntity(true, true, false));
        assertThat(r2.getFields()).hasSize(3);
        assertThat(r2.getFields())
                .containsEntry(text.getCode(), new EvaluatedFieldRightEntity(true, true));
        assertThat(r2.getFields())
                .containsEntry(number.getCode(), new EvaluatedFieldRightEntity(true, false));
        assertThat(r2.getFields())
                .containsEntry(userSelect.getCode(), new EvaluatedFieldRightEntity(true, true));
    }

    @Test
    public void getRecordAcl_getRecordAclPreview() {
        KintoneClient client = setupDefaultClient();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "getRecordAcl_getRecordAclPreview");
        app.addFields(number, userSelect);

        String query = number.getCode() + " >= 10";
        RecordAclBuilder builder = new RecordAclBuilder();
        builder.target(query).user(getDefaultUser(), true, false, false).everyone(false, false, false);
        builder.any().field(userSelect, true, false, true).everyone(true, true, true);
        app.updateRecordAcl(builder).deploy();
        long revision = app.getAppRevision(true);

        RecordRight r1 =
                right(
                        query,
                        entity(EntityType.USER, getDefaultUser(), true, false, false),
                        entity(EntityType.GROUP, "everyone", false, false, false));
        RecordRight r2 =
                right(
                        "",
                        entity(EntityType.FIELD_ENTITY, userSelect.getCode(), true, false, true),
                        entity(EntityType.GROUP, "everyone", true, true, true));

        GetRecordAclRequest req1 = new GetRecordAclRequest();
        req1.setApp(app.id());
        GetRecordAclResponseBody resp1 = client.app().getRecordAcl(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.getRights()).containsExactly(r1, r2);

        builder = new RecordAclBuilder().any().everyone(true, true, false);
        app.updateRecordAcl(builder).deploy();
        RecordRight r3 = right("", entity(EntityType.GROUP, "everyone", true, true, false));

        GetRecordAclPreviewRequest req2 = new GetRecordAclPreviewRequest();
        req2.setApp(app.id());
        GetRecordAclPreviewResponseBody resp2 = client.app().getRecordAclPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getRights()).containsExactly(r3);
    }

    @Test
    public void updateRecordAcl() {
        KintoneClient client = setupDefaultClient();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "updateRecordAcl");
        app.addFields(number, userSelect).deploy();
        long revision = app.getAppRevision(true);

        String query = number.getCode() + " >= 10";
        RecordRight r1 =
                right(
                        query,
                        entity(EntityType.USER, getDefaultUser(), true, true, true),
                        entity(EntityType.GROUP, "everyone", false, false, false));
        RecordRight r2 =
                right(
                        "",
                        entity(EntityType.FIELD_ENTITY, userSelect.getCode(), true, false, false),
                        entity(EntityType.GROUP, "everyone", true, true, true));

        UpdateRecordAclRequest req = new UpdateRecordAclRequest();
        req.setApp(app.id());
        req.setRevision(revision);
        req.setRights(Arrays.asList(r1, r2));
        UpdateRecordAclResponseBody resp = client.app().updateRecordAcl(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        List<RecordRight> previewRights = app.getRecordAcl(true);
        assertThat(previewRights).containsExactly(r1, r2);

        List<RecordRight> deployedRights = app.getRecordAcl(false);
        assertThat(deployedRights).isEmpty();
    }

    private RecordRight right(String query, RecordRightEntity... entities) {
        return new RecordRight().setFilterCond(query).setEntities(Arrays.asList(entities));
    }

    private RecordRightEntity entity(
            EntityType type, String code, boolean viewable, boolean editable, boolean deletable) {
        return new RecordRightEntity()
                .setEntity(new Entity(type, code))
                .setViewable(viewable)
                .setEditable(editable)
                .setDeletable(deletable)
                .setIncludeSubs(false);
    }
}
