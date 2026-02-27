package com.kintone.client.bulk;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.Users;
import com.kintone.client.api.common.BulkRequestsRequest;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import com.kintone.client.api.record.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.model.User;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.record.*;
import com.kintone.client.model.record.Record;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** bulkRequestのテスト */
public class BulkApiTest extends ApiTestBase {
    @Test
    public void bulkRequests() {
        KintoneClient client = setupDefaultClient();
        FieldProperty key = Fields.text("key").setUnique(true);
        FieldProperty field = Fields.text();
        App app = App.create(client, "bulkRequests");
        app.applyExampleProcessManagement().addFields(key, field).deploy();
        long recordId1 = app.addRecord(key, "aaa", field, "initial value 0");
        long recordId2 = app.addRecord(key, "bbb", field, "initial value 1");
        long recordId3 = app.addRecord(key, "ccc", field, "initial value 2");
        long recordId4 = app.addRecord(key, "ddd", field, "initial value 3");
        long recordId5 = app.addRecord(key, "eee", field, "initial value 4");
        long recordId6 = recordId5 + 1;
        long recordId7 = recordId6 + 1;
        long recordId8 = recordId7 + 1;
        BulkRequestsRequest req = new BulkRequestsRequest();

        AddRecordRequest req1 = new AddRecordRequest();
        req1.setApp(app.id());
        req1.setRecord(new Record().putField(field.getCode(), new SingleLineTextFieldValue("add")));
        req.registerAddRecord(req1);

        AddRecordsRequest req2 = new AddRecordsRequest();
        Record r1 = new Record().putField(field.getCode(), new SingleLineTextFieldValue("adds 1"));
        Record r2 = new Record().putField(field.getCode(), new SingleLineTextFieldValue("adds 2"));
        req2.setApp(app.id());
        req2.setRecords(Arrays.asList(r1, r2));
        req.registerAddRecords(req2);

        DeleteRecordsRequest req3 = new DeleteRecordsRequest();
        req3.setApp(app.id());
        req3.setIds(Collections.singletonList(recordId1));
        req3.setRevisions(Collections.singletonList(1L));
        req.registerDeleteRecords(req3);

        UpdateRecordRequest req4 = new UpdateRecordRequest();
        req4.setApp(app.id());
        req4.setId(recordId2);
        req4.setRecord(new Record().putField(field.getCode(), new SingleLineTextFieldValue("update")));
        req4.setRevision(1L);
        req.registerUpdateRecord(req4);

        UpdateRecordRequest req5 = new UpdateRecordRequest();
        req5.setApp(app.id());
        req5.setUpdateKey(new UpdateKey(key.getCode(), "ccc"));
        req5.setRecord(
                new Record().putField(field.getCode(), new SingleLineTextFieldValue("update by key")));
        req5.setRevision(1L);
        req.registerUpdateRecord(req5);

        RecordForUpdate up1 =
                new RecordForUpdate(
                        recordId4,
                        new Record().putField(field.getCode(), new SingleLineTextFieldValue("updates 1")),
                        1L);
        RecordForUpdate up2 =
                new RecordForUpdate(
                        new UpdateKey(key.getCode(), "eee"),
                        new Record().putField(field.getCode(), new SingleLineTextFieldValue("updates 2")),
                        1L);
        UpdateRecordsRequest req6 = new UpdateRecordsRequest();
        req6.setApp(app.id());
        req6.setRecords(Arrays.asList(up1, up2));
        req.registerUpdateRecords(req6);

        UpdateRecordAssigneesRequest req7 = new UpdateRecordAssigneesRequest();
        req7.setApp(app.id());
        req7.setId(recordId2);
        req7.setAssignees(Collections.singletonList(Users.cybozu.getCode()));
        req7.setRevision(2L); // req4で更新されるため
        req.registerUpdateRecordAssignees(req7);

        UpdateRecordStatusRequest req8 = new UpdateRecordStatusRequest();
        req8.setApp(app.id());
        req8.setId(recordId3);
        req8.setAction("action 1");
        req8.setRevision(2L); // req5で更新されるため
        req.registerUpdateRecordStatus(req8);

        StatusAction a1 =
                new StatusAction().setId(recordId4).setAction("action 1").setRevision(2L); // req6で更新されるため
        StatusAction a2 = new StatusAction().setId(recordId5).setAction("action 1").setRevision(2L);
        UpdateRecordStatusesRequest req9 = new UpdateRecordStatusesRequest();
        req9.setApp(app.id());
        req9.setRecords(Arrays.asList(a1, a2));
        req.registerUpdateRecordStatuses(req9);

        // bulkRequest実行
        BulkRequestsResponseBody resp = client.bulkRequests(req);
        assertThat(resp.getResults()).hasSize(9);

        AddRecordResponseBody resp1 = (AddRecordResponseBody) resp.getResults().get(0);
        assertThat(resp1.getId()).isEqualTo(recordId6);
        assertThat(resp1.getRevision()).isEqualTo(1L);

        AddRecordsResponseBody resp2 = (AddRecordsResponseBody) resp.getResults().get(1);
        assertThat(resp2.getIds()).containsExactly(recordId7, recordId8);
        assertThat(resp2.getRevisions()).containsExactly(1L, 1L);

        // delete結果は特にないので何も確認しない
        DeleteRecordsResponseBody resp3 = (DeleteRecordsResponseBody) resp.getResults().get(2);

        UpdateRecordResponseBody resp4 = (UpdateRecordResponseBody) resp.getResults().get(3);
        assertThat(resp4.getRevision()).isEqualTo(2L);

        UpdateRecordResponseBody resp5 = (UpdateRecordResponseBody) resp.getResults().get(4);
        assertThat(resp5.getRevision()).isEqualTo(2L);

        UpdateRecordsResponseBody resp6 = (UpdateRecordsResponseBody) resp.getResults().get(5);
        assertThat(resp6.getRecords()).hasSize(2);
        assertThat(resp6.getRecords().get(0).getId()).isEqualTo(recordId4);
        assertThat(resp6.getRecords().get(0).getRevision()).isEqualTo(2L);
        assertThat(resp6.getRecords().get(1).getId()).isEqualTo(recordId5);
        assertThat(resp6.getRecords().get(1).getRevision()).isEqualTo(2L);

        UpdateRecordAssigneesResponseBody resp7 =
                (UpdateRecordAssigneesResponseBody) resp.getResults().get(6);
        assertThat(resp7.getRevision()).isEqualTo(3);

        UpdateRecordStatusResponseBody resp8 =
                (UpdateRecordStatusResponseBody) resp.getResults().get(7);
        assertThat(resp8.getRevision()).isEqualTo(4); // ステータス変更は2進む

        UpdateRecordStatusesResponseBody resp9 =
                (UpdateRecordStatusesResponseBody) resp.getResults().get(8);
        assertThat(resp9.getRecords()).hasSize(2);
        assertThat(resp9.getRecords().get(0).getId()).isEqualTo(recordId4);
        assertThat(resp9.getRecords().get(0).getRevision()).isEqualTo(4); // ステータス変更は2進む
        assertThat(resp9.getRecords().get(1).getId()).isEqualTo(recordId5);
        assertThat(resp9.getRecords().get(1).getRevision()).isEqualTo(4);

        List<Record> records = app.getRecords();
        // recordId1が削除され、3レコード増えるので7件
        assertThat(records).hasSize(7);

        // 追加分の確認
        assertThat(records.get(0).getId()).isEqualTo(recordId8);
        assertThat(records.get(0).getSingleLineTextFieldValue(field.getCode())).isEqualTo("adds 2");

        assertThat(records.get(1).getId()).isEqualTo(recordId7);
        assertThat(records.get(1).getSingleLineTextFieldValue(field.getCode())).isEqualTo("adds 1");

        assertThat(records.get(2).getId()).isEqualTo(recordId6);
        assertThat(records.get(2).getSingleLineTextFieldValue(field.getCode())).isEqualTo("add");

        // 更新分の確認
        assertThat(records.get(3).getId()).isEqualTo(recordId5);
        assertThat(records.get(3).getSingleLineTextFieldValue(key.getCode())).isEqualTo("eee");
        assertThat(records.get(3).getSingleLineTextFieldValue(field.getCode())).isEqualTo("updates 2");
        assertThat(records.get(3).getStatusFieldValue()).isEqualTo("state B");
        assertThat(getAssigneeCodes(records.get(3))).isEmpty();

        assertThat(records.get(4).getId()).isEqualTo(recordId4);
        assertThat(records.get(4).getSingleLineTextFieldValue(key.getCode())).isEqualTo("ddd");
        assertThat(records.get(4).getSingleLineTextFieldValue(field.getCode())).isEqualTo("updates 1");
        assertThat(records.get(4).getStatusFieldValue()).isEqualTo("state B");
        assertThat(getAssigneeCodes(records.get(4))).isEmpty();

        assertThat(records.get(5).getId()).isEqualTo(recordId3);
        assertThat(records.get(5).getSingleLineTextFieldValue(key.getCode())).isEqualTo("ccc");
        assertThat(records.get(5).getSingleLineTextFieldValue(field.getCode()))
                .isEqualTo("update by key");
        assertThat(records.get(5).getStatusFieldValue()).isEqualTo("state B");
        assertThat(getAssigneeCodes(records.get(5))).isEmpty();

        assertThat(records.get(6).getId()).isEqualTo(recordId2);
        assertThat(records.get(6).getSingleLineTextFieldValue(key.getCode())).isEqualTo("bbb");
        assertThat(records.get(6).getSingleLineTextFieldValue(field.getCode())).isEqualTo("update");
        assertThat(records.get(6).getStatusFieldValue()).isEqualTo("state A");
        assertThat(getAssigneeCodes(records.get(6))).containsExactly(Users.cybozu.getCode());
    }

    private List<String> getAssigneeCodes(Record record) {
        List<User> assignees = record.getStatusAssigneeFieldValue();
        return assignees.stream().map(User::getCode).collect(Collectors.toList());
    }
}
