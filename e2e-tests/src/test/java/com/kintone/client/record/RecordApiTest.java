package com.kintone.client.record;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.Users;
import com.kintone.client.api.record.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.Order;
import com.kintone.client.model.User;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.record.*;
import com.kintone.client.model.record.Record;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/** RecordClientのテスト */
public class RecordApiTest extends ApiTestBase {

    private List<Record> setupRecords(String textFieldCode, int size) {
        return IntStream.range(0, size)
                .mapToObj(
                        i -> new Record().putField(textFieldCode, new SingleLineTextFieldValue("value " + i)))
                .collect(Collectors.toList());
    }

    @Test
    public void addRecord() {
        KintoneClient client = setupDefaultClient();
        FieldProperty field = Fields.text();
        App app = App.create(client, "addRecord");
        app.addFields(field).deploy();

        Record record = setupRecords(field.getCode(), 1).get(0);
        AddRecordRequest req = new AddRecordRequest();
        req.setApp(app.id());
        req.setRecord(record);
        AddRecordResponseBody resp = client.record().addRecord(req);
        assertThat(resp.getRevision()).isEqualTo(1L);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getId()).isEqualTo(resp.getId());
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 0");
    }

    @Test
    public void addRecordComment() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "addRecordComment").deploy();
        long recordId = app.addRecord();

        RecordComment comment = new RecordComment("Record Comment");
        List<Entity> mentions = new ArrayList<>();
        mentions.add(new Entity(EntityType.USER, Users.user1.getCode()));
        comment.setMentions(mentions);
        AddRecordCommentRequest req =
                new AddRecordCommentRequest().setApp(app.id()).setRecord(recordId).setComment(comment);
        long id = client.record().addRecordComment(req).getId();
        assertThat(id).isEqualTo(1L);

        List<PostedRecordComment> comments = client.record().getRecordComments(app.id(), recordId);
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getId()).isEqualTo(id);
        assertThat(comments.get(0).getCreator().getCode()).isEqualTo(getDefaultUser());
        assertThat(comments.get(0).getText())
                .isEqualTo(Users.user1.getName() + " \n" + "Record Comment ");
        assertThat(comments.get(0).getMentions())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(new Entity(EntityType.USER, Users.user1.getCode()));
    }

    @Test
    public void addRecords() {
        KintoneClient client = setupDefaultClient();
        FieldProperty field = Fields.text();
        App app = App.create(client, "addRecords");
        app.addFields(field).deploy();

        AddRecordsRequest req = new AddRecordsRequest();
        req.setApp(app.id());
        req.setRecords(setupRecords(field.getCode(), 2));
        AddRecordsResponseBody resp = client.record().addRecords(req);
        assertThat(resp.getIds()).containsExactly(1L, 2L);
        assertThat(resp.getRevisions()).containsExactly(1L, 1L);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(2);
        assertThat(records.get(0).getId()).isEqualTo(2L);
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 1");
        assertThat(records.get(1).getId()).isEqualTo(1L);
        assertThat(records.get(1).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 0");
    }

    @Test
    public void createCursor_getRecordsByCursor_deleteCursor() {
        KintoneClient client = setupDefaultClient();
        FieldProperty field = Fields.text();
        App app = App.create(client, "createCursor");
        app.addFields(field, Fields.text("text2")).deploy();
        app.addRecords(setupRecords(field.getCode(), 5));

        CreateCursorRequest req1 = new CreateCursorRequest();
        req1.setApp(app.id());
        req1.setFields(Arrays.asList("$id", field.getCode()));
        req1.setQuery("$id > 1");
        req1.setSize(3L);
        CreateCursorResponseBody resp1 = client.record().createCursor(req1);
        assertThat(resp1.getTotalCount()).isEqualTo(4);
        String cursorId = resp1.getId();

        GetRecordsByCursorRequest req2 = new GetRecordsByCursorRequest();
        req2.setId(cursorId);
        GetRecordsByCursorResponseBody resp2 = client.record().getRecordsByCursor(req2);
        List<Record> records = resp2.getRecords();
        assertThat(records).hasSize(3);
        assertThat(records.get(0).getId()).isEqualTo(5L);
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 4");
        assertThat(records.get(0).getFieldCodes(false)).containsExactly(field.getCode());
        assertThat(records.get(1).getId()).isEqualTo(4L);
        assertThat(records.get(1).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 3");
        assertThat(records.get(1).getFieldCodes(false)).containsExactly(field.getCode());
        assertThat(records.get(2).getId()).isEqualTo(3L);
        assertThat(records.get(2).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 2");
        assertThat(records.get(2).getFieldCodes(false)).containsExactly(field.getCode());

        DeleteCursorRequest req3 = new DeleteCursorRequest();
        req3.setId(cursorId);
        client.record().deleteCursor(req3);
    }

    @Test
    public void deleteRecordComment() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "deleteRecordComment").deploy();
        long recordId = app.addRecord();
        long commentId =
                client.record().addRecordComment(app.id(), recordId, new RecordComment("comment"));

        DeleteRecordCommentRequest req =
                new DeleteRecordCommentRequest().setApp(app.id()).setRecord(recordId).setComment(commentId);
        client.record().deleteRecordComment(req);

        List<PostedRecordComment> comments = client.record().getRecordComments(app.id(), recordId);
        assertThat(comments).isEmpty();
    }

    @Test
    public void deleteRecords() {
        KintoneClient client = setupDefaultClient();
        FieldProperty field = Fields.text();
        App app = App.create(client, "addRecords");
        app.addFields(field).deploy();
        List<Long> recordIds = app.addRecords(setupRecords(field.getCode(), 3));

        DeleteRecordsRequest req = new DeleteRecordsRequest();
        req.setApp(app.id());
        req.setIds(recordIds);
        req.setRevisions(Arrays.asList(1L, 1L, 1L));
        client.record().deleteRecords(req);

        assertThat(app.getRecords()).isEmpty();
    }

    @Test
    public void getRecord() {
        KintoneClient client = setupDefaultClient();
        FieldProperty field = Fields.text();
        App app = App.create(client, "getRecord");
        app.addFields(field).deploy();
        long recordId = app.addRecord(field, "text");

        GetRecordRequest req = new GetRecordRequest();
        req.setApp(app.id());
        req.setId(recordId);
        GetRecordResponseBody resp = client.record().getRecord(req);
        assertThat(resp.getRecord().getId()).isEqualTo(recordId);
        assertThat(resp.getRecord().getSingleLineTextFieldValue(field.getCode())).isEqualTo("text");
    }

    @Test
    public void getRecordComments() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "getRecordComment").deploy();
        long recordId = app.addRecord();
        for (int i = 0; i < 4; i++) {
            RecordComment comment = new RecordComment("comment " + i);
            client.record().addRecordComment(app.id(), recordId, comment);
        }

        GetRecordCommentsRequest req = new GetRecordCommentsRequest();
        req.setApp(app.id());
        req.setRecord(recordId);
        req.setLimit(2L);
        req.setOffset(1L);
        req.setOrder(Order.DESC);
        GetRecordCommentsResponseBody resp = client.record().getRecordComments(req);
        List<PostedRecordComment> comments = resp.getComments();
        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getId()).isEqualTo(3L);
        assertThat(comments.get(0).getText()).startsWith("comment 2");
        assertThat(comments.get(1).getId()).isEqualTo(2L);
        assertThat(comments.get(1).getText()).startsWith("comment 1");
    }

    @Test
    public void getRecords() {
        KintoneClient client = setupDefaultClient();
        FieldProperty field = Fields.text();
        App app = App.create(client, "createCursor");
        app.addFields(field, Fields.text("text2")).deploy();
        app.addRecords(setupRecords(field.getCode(), 5));

        GetRecordsRequest req = new GetRecordsRequest();
        req.setApp(app.id());
        req.setFields(Arrays.asList("$id", field.getCode()));
        req.setQuery("$id > 1 limit 3");
        req.setTotalCount(true);
        GetRecordsResponseBody resp = client.record().getRecords(req);
        assertThat(resp.getTotalCount()).isEqualTo(4);
        List<Record> records = resp.getRecords();
        assertThat(records).hasSize(3);
        assertThat(records.get(0).getId()).isEqualTo(5L);
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 4");
        assertThat(records.get(0).getFieldCodes(false)).containsExactly(field.getCode());
        assertThat(records.get(1).getId()).isEqualTo(4L);
        assertThat(records.get(1).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 3");
        assertThat(records.get(1).getFieldCodes(false)).containsExactly(field.getCode());
        assertThat(records.get(2).getId()).isEqualTo(3L);
        assertThat(records.get(2).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 2");
        assertThat(records.get(2).getFieldCodes(false)).containsExactly(field.getCode());
    }

    @Test
    public void updateRecord() {
        KintoneClient client = setupDefaultClient();
        FieldProperty field = Fields.text();
        App app = App.create(client, "updateRecord");
        app.addFields(field).deploy();
        long recordId = app.addRecord(field, "initial value");

        UpdateRecordRequest req = new UpdateRecordRequest();
        req.setApp(app.id());
        req.setId(recordId);
        req.setRevision(1L);
        req.setRecord(setupRecords(field.getCode(), 1).get(0));
        UpdateRecordResponseBody resp = client.record().updateRecord(req);
        assertThat(resp.getRevision()).isEqualTo(2L);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getId()).isEqualTo(recordId);
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 0");
    }

    @Test
    public void updateRecord_updateKey() {
        KintoneClient client = setupDefaultClient();
        FieldProperty key = Fields.text("key").setUnique(true);
        FieldProperty field = Fields.text();
        App app = App.create(client, "updateRecord_updateKey");
        app.addFields(key, field).deploy();
        long recordId1 = app.addRecord(key, "abc", field, "initial value 0");
        long recordId2 = app.addRecord(key, "def", field, "initial value 1");

        UpdateRecordRequest req = new UpdateRecordRequest();
        req.setApp(app.id());
        req.setUpdateKey(new UpdateKey(key.getCode(), "abc"));
        req.setRevision(1L);
        req.setRecord(setupRecords(field.getCode(), 1).get(0));
        UpdateRecordResponseBody resp = client.record().updateRecord(req);
        assertThat(resp.getRevision()).isEqualTo(2L);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(2);
        assertThat(records.get(0).getId()).isEqualTo(recordId2);
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode()))
                .isEqualTo("initial value 1");
        assertThat(records.get(1).getId()).isEqualTo(recordId1);
        assertThat(records.get(1).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 0");
    }

    @Test
    public void updateRecordAssignees() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "updateRecordAssignees");
        app.applyExampleProcessManagement().deploy();
        long recordId = app.addRecord();

        UpdateRecordAssigneesRequest req = new UpdateRecordAssigneesRequest();
        req.setApp(app.id());
        req.setId(recordId);
        req.setAssignees(Collections.singletonList(Users.cybozu.getCode()));
        req.setRevision(1L);
        UpdateRecordAssigneesResponseBody resp = client.record().updateRecordAssignees(req);
        assertThat(resp.getRevision()).isEqualTo(2L);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getId()).isEqualTo(recordId);
        List<User> assignees = records.get(0).getStatusAssigneeFieldValue();
        List<String> codes = assignees.stream().map(User::getCode).collect(Collectors.toList());
        assertThat(codes).containsExactly(Users.cybozu.getCode());
    }

    @Test
    public void updateRecords() {
        KintoneClient client = setupDefaultClient();
        FieldProperty key = Fields.text("key").setUnique(true);
        FieldProperty field = Fields.text();
        App app = App.create(client, "updateRecords");
        app.addFields(key, field).deploy();
        long recordId1 = app.addRecord(key, "abc", field, "initial value 0");
        long recordId2 = app.addRecord(key, "def", field, "initial value 1");

        RecordForUpdate up1 =
                new RecordForUpdate(
                        recordId1,
                        new Record().putField(field.getCode(), new SingleLineTextFieldValue("value 0")),
                        1L);
        RecordForUpdate up2 =
                new RecordForUpdate(
                        new UpdateKey(key.getCode(), "def"),
                        new Record().putField(field.getCode(), new SingleLineTextFieldValue("value 1")),
                        1L);
        UpdateRecordsRequest req = new UpdateRecordsRequest();
        req.setApp(app.id());
        req.setRecords(Arrays.asList(up1, up2));
        UpdateRecordsResponseBody resp = client.record().updateRecords(req);
        assertThat(resp.getRecords()).hasSize(2);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(2);
        assertThat(records.get(0).getId()).isEqualTo(recordId2);
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 1");
        assertThat(records.get(1).getId()).isEqualTo(recordId1);
        assertThat(records.get(1).getSingleLineTextFieldValue(field.getCode())).isEqualTo("value 0");
    }

    @Test
    public void updateRecordStatus() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "updateRecordStatus");
        app.applyExampleProcessManagement().deploy();
        long recordId = app.addRecord();

        UpdateRecordStatusRequest req = new UpdateRecordStatusRequest();
        req.setApp(app.id());
        req.setId(recordId);
        req.setAction("action 1");
        req.setRevision(1L);
        UpdateRecordStatusResponseBody resp = client.record().updateRecordStatus(req);
        assertThat(resp.getRevision()).isEqualTo(3L);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getId()).isEqualTo(recordId);
        assertThat(records.get(0).getStatusFieldValue()).isEqualTo("state B");
    }

    @Test
    public void updateRecordStatuses() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "updateRecordStatuses");
        app.applyExampleProcessManagement().deploy();
        long recordId1 = app.addRecord();
        long recordId2 = app.addRecord();

        StatusAction a1 = new StatusAction().setId(recordId1).setAction("action 1").setRevision(1L);
        StatusAction a2 = new StatusAction().setId(recordId2).setAction("action 1").setRevision(1L);
        UpdateRecordStatusesRequest req = new UpdateRecordStatusesRequest();
        req.setApp(app.id());
        req.setRecords(Arrays.asList(a1, a2));
        UpdateRecordStatusesResponseBody resp = client.record().updateRecordStatuses(req);
        List<RecordRevision> revisions = resp.getRecords();
        assertThat(revisions).hasSize(2);
        assertThat(revisions.get(0).getId()).isEqualTo(recordId1);
        assertThat(revisions.get(0).getRevision()).isEqualTo(3L);
        assertThat(revisions.get(1).getId()).isEqualTo(recordId2);
        assertThat(revisions.get(1).getRevision()).isEqualTo(3L);

        List<Record> records = app.getRecords();
        assertThat(records).hasSize(2);
        assertThat(records.get(0).getId()).isEqualTo(recordId2);
        assertThat(records.get(0).getStatusFieldValue()).isEqualTo("state B");
        assertThat(records.get(1).getId()).isEqualTo(recordId1);
        assertThat(records.get(1).getStatusFieldValue()).isEqualTo("state B");
    }

    @Test
    public void upsertRecords() {
        KintoneClient client = setupDefaultClient();
        FieldProperty key = Fields.text("key").setUnique(true);
        FieldProperty field = Fields.text();
        App app = App.create(client, "upsertRecords");
        app.addFields(key, field).deploy();

        // Insert: 新規レコードを作成
        long existingRecordId = app.addRecord(key, "existing_key", field, "initial value");

        // Upsert: 既存レコード (updateKeyで更新) と新規レコード (INSERT) を同時に処理
        RecordForUpdate updateExisting =
                new RecordForUpdate(
                        new UpdateKey(key.getCode(), "existing_key"),
                        new Record().putField(field.getCode(), new SingleLineTextFieldValue("updated value")));
        RecordForUpdate insertNew =
                new RecordForUpdate(
                        new UpdateKey(key.getCode(), "new_key"),
                        new Record().putField(field.getCode(), new SingleLineTextFieldValue("new value")));

        UpsertRecordsRequest req = new UpsertRecordsRequest();
        req.setApp(app.id());
        req.setRecords(Arrays.asList(updateExisting, insertNew));
        UpsertRecordsResponseBody resp = client.record().upsertRecords(req);

        List<RecordUpsertResult> results = resp.getRecords();
        assertThat(results).hasSize(2);

        // 既存レコードの更新結果
        assertThat(results.get(0).getId()).isEqualTo(existingRecordId);
        assertThat(results.get(0).getRevision()).isEqualTo(2L);
        assertThat(results.get(0).getOperation()).isEqualTo(RecordOperationType.UPDATE);

        // 新規レコードの作成結果
        assertThat(results.get(1).getId()).isGreaterThan(existingRecordId);
        assertThat(results.get(1).getRevision()).isEqualTo(1L);
        assertThat(results.get(1).getOperation()).isEqualTo(RecordOperationType.INSERT);

        // レコードの内容を確認
        List<Record> records = app.getRecords();
        assertThat(records).hasSize(2);

        Record updatedRecord =
                records.stream()
                        .filter(r -> r.getId() == existingRecordId)
                        .findFirst()
                        .orElseThrow(AssertionError::new);
        assertThat(updatedRecord.getSingleLineTextFieldValue(key.getCode())).isEqualTo("existing_key");
        assertThat(updatedRecord.getSingleLineTextFieldValue(field.getCode()))
                .isEqualTo("updated value");

        Record newRecord =
                records.stream()
                        .filter(r -> r.getId() == results.get(1).getId())
                        .findFirst()
                        .orElseThrow(AssertionError::new);
        assertThat(newRecord.getSingleLineTextFieldValue(key.getCode())).isEqualTo("new_key");
        assertThat(newRecord.getSingleLineTextFieldValue(field.getCode())).isEqualTo("new value");
    }
}
