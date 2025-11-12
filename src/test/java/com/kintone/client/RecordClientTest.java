package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kintone.client.api.record.AddRecordCommentRequest;
import com.kintone.client.api.record.AddRecordCommentResponseBody;
import com.kintone.client.api.record.AddRecordRequest;
import com.kintone.client.api.record.AddRecordResponseBody;
import com.kintone.client.api.record.AddRecordsRequest;
import com.kintone.client.api.record.AddRecordsResponseBody;
import com.kintone.client.api.record.CreateCursorRequest;
import com.kintone.client.api.record.CreateCursorResponseBody;
import com.kintone.client.api.record.DeleteCursorRequest;
import com.kintone.client.api.record.DeleteCursorResponseBody;
import com.kintone.client.api.record.DeleteRecordCommentRequest;
import com.kintone.client.api.record.DeleteRecordCommentResponseBody;
import com.kintone.client.api.record.DeleteRecordsRequest;
import com.kintone.client.api.record.DeleteRecordsResponseBody;
import com.kintone.client.api.record.GetRecordCommentsRequest;
import com.kintone.client.api.record.GetRecordCommentsResponseBody;
import com.kintone.client.api.record.GetRecordRequest;
import com.kintone.client.api.record.GetRecordResponseBody;
import com.kintone.client.api.record.GetRecordsByCursorRequest;
import com.kintone.client.api.record.GetRecordsByCursorResponseBody;
import com.kintone.client.api.record.GetRecordsRequest;
import com.kintone.client.api.record.GetRecordsResponseBody;
import com.kintone.client.api.record.UpdateRecordAssigneesRequest;
import com.kintone.client.api.record.UpdateRecordAssigneesResponseBody;
import com.kintone.client.api.record.UpdateRecordRequest;
import com.kintone.client.api.record.UpdateRecordResponseBody;
import com.kintone.client.api.record.UpdateRecordStatusRequest;
import com.kintone.client.api.record.UpdateRecordStatusResponseBody;
import com.kintone.client.api.record.UpdateRecordStatusesRequest;
import com.kintone.client.api.record.UpdateRecordStatusesResponseBody;
import com.kintone.client.api.record.UpdateRecordsRequest;
import com.kintone.client.api.record.UpdateRecordsResponseBody;
import com.kintone.client.model.Order;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.RecordComment;
import com.kintone.client.model.record.RecordForUpdate;
import com.kintone.client.model.record.RecordOperationType;
import com.kintone.client.model.record.RecordUpdateResult;
import com.kintone.client.model.record.StatusAction;
import com.kintone.client.model.record.UpdateKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

public class RecordClientTest {

    private InternalClientMock mockClient = new InternalClientMock();
    private RecordClient sut = new RecordClient(mockClient, Collections.emptyList());

    @Test
    public void addRecord_long_Record() {
        mockClient.setResponseBody(new AddRecordResponseBody(100, 1));

        assertThat(sut.addRecord(1, new Record())).isEqualTo(100L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_RECORD);
        assertThat(mockClient.getLastBody()).isInstanceOf(AddRecordRequest.class);
        AddRecordRequest req = (AddRecordRequest) mockClient.getLastBody();
        assertThat(req.getApp()).isEqualTo(1L);
        assertThat(req.getRecord()).usingRecursiveComparison().isEqualTo(new Record());
    }

    @Test
    public void addRecord_AddRecordRequest() {
        AddRecordRequest req = new AddRecordRequest();
        AddRecordResponseBody resp = new AddRecordResponseBody(1, 2);
        mockClient.setResponseBody(resp);

        assertThat(sut.addRecord(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_RECORD);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void addRecordComment_long_long_RecordComment() {
        mockClient.setResponseBody(new AddRecordCommentResponseBody(10));

        RecordComment comment = new RecordComment().setText("comment");
        assertThat(sut.addRecordComment(1, 100L, comment)).isEqualTo(10L);

        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_RECORD_COMMENT);
        assertThat(mockClient.getLastBody()).isInstanceOf(AddRecordCommentRequest.class);
        AddRecordCommentRequest expected =
                new AddRecordCommentRequest().setApp(1L).setRecord(100L).setComment(comment);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void addRecordComment_AddRecordCommentRequest() {
        AddRecordCommentRequest req = new AddRecordCommentRequest();
        AddRecordCommentResponseBody resp = new AddRecordCommentResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.addRecordComment(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_RECORD_COMMENT);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void addRecords_long_List() {
        List<Long> ids = Arrays.asList(100L, 101L);
        List<Long> revisions = Arrays.asList(1L, 1L);
        mockClient.setResponseBody(new AddRecordsResponseBody(ids, revisions));

        List<Record> records = Arrays.asList(new Record(), new Record());
        assertThat(sut.addRecords(1, records)).containsExactly(100L, 101L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_RECORDS);
        assertThat(mockClient.getLastBody()).isInstanceOf(AddRecordsRequest.class);
        AddRecordsRequest expected = new AddRecordsRequest().setApp(1L).setRecords(records);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void addRecords_AddRecordsRequest() {
        AddRecordsRequest req = new AddRecordsRequest();
        AddRecordsResponseBody resp =
                new AddRecordsResponseBody(Collections.emptyList(), Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.addRecords(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_RECORDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void countRecords_long() {
        mockClient.setResponseBody(new GetRecordsResponseBody(Collections.emptyList(), 10L));

        assertThat(sut.countRecords(1)).isEqualTo(10L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        GetRecordsRequest expected =
                new GetRecordsRequest().setApp(1L).setFields(null).setQuery(null).setTotalCount(true);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void countRecords_long_String() {
        mockClient.setResponseBody(new GetRecordsResponseBody(Collections.emptyList(), 10L));

        assertThat(sut.countRecords(1, "$id = 10")).isEqualTo(10L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        GetRecordsRequest expected =
                new GetRecordsRequest().setApp(1L).setFields(null).setQuery("$id = 10").setTotalCount(true);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void createCursor_long() {
        mockClient.setResponseBody(new CreateCursorResponseBody("id", 10L));

        assertThat(sut.createCursor(18L)).isEqualTo("id");
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.CREATE_CURSOR);
        assertThat(mockClient.getLastBody()).isInstanceOf(CreateCursorRequest.class);
        CreateCursorRequest expected = new CreateCursorRequest().setApp(18L);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void createCursor_long_List_String() {
        mockClient.setResponseBody(new CreateCursorResponseBody("id", 10L));

        assertThat(sut.createCursor(20L, Lists.newArrayList("$id"), "$id < 100")).isEqualTo("id");
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.CREATE_CURSOR);
        assertThat(mockClient.getLastBody()).isInstanceOf(CreateCursorRequest.class);
        CreateCursorRequest expected =
                new CreateCursorRequest()
                        .setApp(20L)
                        .setFields(Lists.newArrayList("$id"))
                        .setQuery("$id < 100");
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void createCursor_CreateCursorRequest() {
        CreateCursorRequest req = new CreateCursorRequest();
        CreateCursorResponseBody resp = new CreateCursorResponseBody("", 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.createCursor(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.CREATE_CURSOR);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void deleteCursor_String() {
        mockClient.setResponseBody(new DeleteCursorResponseBody());

        sut.deleteCursor("cursor_id");
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_CURSOR);
        assertThat(mockClient.getLastBody()).isInstanceOf(DeleteCursorRequest.class);
        DeleteCursorRequest expected = new DeleteCursorRequest().setId("cursor_id");
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void deleteCursor_DeleteCursorRequest() {
        DeleteCursorRequest req = new DeleteCursorRequest();
        DeleteCursorResponseBody resp = new DeleteCursorResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.deleteCursor(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_CURSOR);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void deleteRecordComment_long_long_long() {
        mockClient.setResponseBody(new DeleteRecordCommentResponseBody());

        sut.deleteRecordComment(1, 100L, 10L);

        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_RECORD_COMMENT);
        assertThat(mockClient.getLastBody()).isInstanceOf(DeleteRecordCommentRequest.class);
        DeleteRecordCommentRequest expected =
                new DeleteRecordCommentRequest().setApp(1L).setRecord(100L).setComment(10L);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void deleteRecordComment_DeleteRecordCommentRequest() {
        DeleteRecordCommentRequest req = new DeleteRecordCommentRequest();
        DeleteRecordCommentResponseBody resp = new DeleteRecordCommentResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.deleteRecordComment(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_RECORD_COMMENT);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void deleteRecords_long_List() {
        mockClient.setResponseBody(new DeleteRecordsResponseBody());

        List<Long> ids = Arrays.asList(100L, 101L);
        sut.deleteRecords(1, ids);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_RECORDS);
        assertThat(mockClient.getLastBody()).isInstanceOf(DeleteRecordsRequest.class);
        DeleteRecordsRequest expected =
                new DeleteRecordsRequest().setApp(1L).setIds(ids).setRevisions(null);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void deleteRecords_long_List_List() {
        mockClient.setResponseBody(new DeleteRecordsResponseBody());

        List<Long> ids = Arrays.asList(100L, 101L);
        List<Long> revisions = Arrays.asList(1L, 2L);
        sut.deleteRecords(1, ids, revisions);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_RECORDS);
        assertThat(mockClient.getLastBody()).isInstanceOf(DeleteRecordsRequest.class);
        DeleteRecordsRequest expected =
                new DeleteRecordsRequest().setApp(1L).setIds(ids).setRevisions(revisions);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void deleteRecords_DeleteRecordsRequest() {
        DeleteRecordsRequest req = new DeleteRecordsRequest();
        DeleteRecordsResponseBody resp = new DeleteRecordsResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.deleteRecords(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_RECORDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRecord_long_long() {
        Record record = new Record(10L, null);
        mockClient.setResponseBody(new GetRecordResponseBody(record));

        assertThat(sut.getRecord(1, 2)).isEqualTo(record);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetRecordRequest().setApp(1L).setId(2L));
    }

    @Test
    public void getRecord_GetRecordRequest() {
        GetRecordRequest req = new GetRecordRequest();
        GetRecordResponseBody resp = new GetRecordResponseBody(new Record());
        mockClient.setResponseBody(resp);

        assertThat(sut.getRecord(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRecordComments_long_long() {
        mockClient.setResponseBody(
                new GetRecordCommentsResponseBody(Collections.emptyList(), false, false));

        assertThat(sut.getRecordComments(1, 100L)).isEmpty();
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_COMMENTS);
        assertThat(mockClient.getLastBody()).isInstanceOf(GetRecordCommentsRequest.class);
        GetRecordCommentsRequest expected =
                new GetRecordCommentsRequest()
                        .setApp(1L)
                        .setRecord(100L)
                        .setLimit(null)
                        .setOffset(null)
                        .setOrder(null);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getRecordComments_long_long_Order_Long_Long() {
        mockClient.setResponseBody(
                new GetRecordCommentsResponseBody(Collections.emptyList(), false, false));

        assertThat(sut.getRecordComments(1, 100L, Order.DESC, 20L, 30L)).isEmpty();
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_COMMENTS);
        assertThat(mockClient.getLastBody()).isInstanceOf(GetRecordCommentsRequest.class);
        GetRecordCommentsRequest expected =
                new GetRecordCommentsRequest()
                        .setApp(1L)
                        .setRecord(100L)
                        .setLimit(30L)
                        .setOffset(20L)
                        .setOrder(Order.DESC);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getRecordComments_GetRecordCommentsRequest() {
        GetRecordCommentsRequest req = new GetRecordCommentsRequest();
        GetRecordCommentsResponseBody resp =
                new GetRecordCommentsResponseBody(Collections.emptyList(), false, false);
        mockClient.setResponseBody(resp);

        assertThat(sut.getRecordComments(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_COMMENTS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRecords_long() {
        mockClient.setResponseBody(new GetRecordsResponseBody(Collections.emptyList(), null));

        assertThat(sut.getRecords(1)).isEmpty();
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        GetRecordsRequest expected =
                new GetRecordsRequest().setApp(1L).setFields(null).setQuery(null).setTotalCount(false);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getRecords_long_int_long() {
        mockClient.setResponseBody(new GetRecordsResponseBody(Collections.emptyList(), null));

        assertThat(sut.getRecords(1, 10, 200)).isEmpty();
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        GetRecordsRequest expected =
                new GetRecordsRequest()
                        .setApp(1L)
                        .setFields(null)
                        .setQuery("limit 10 offset 200")
                        .setTotalCount(false);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getRecords_long_List_int_long() {
        mockClient.setResponseBody(new GetRecordsResponseBody(Collections.emptyList(), null));

        List<String> fields = Arrays.asList("field1", "field2");
        assertThat(sut.getRecords(1, fields, 10, 200)).isEmpty();
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        GetRecordsRequest expected =
                new GetRecordsRequest()
                        .setApp(1L)
                        .setFields(fields)
                        .setQuery("limit 10 offset 200")
                        .setTotalCount(false);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);

        assertThatThrownBy(() -> sut.getRecords(1, fields, 10, 10001))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getRecords_long_String() {
        mockClient.setResponseBody(new GetRecordsResponseBody(Collections.emptyList(), null));

        assertThat(sut.getRecords(1, "$id = 10")).isEmpty();
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        GetRecordsRequest expected =
                new GetRecordsRequest()
                        .setApp(1L)
                        .setFields(null)
                        .setQuery("$id = 10")
                        .setTotalCount(false);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getRecords_long_List_String() {
        mockClient.setResponseBody(new GetRecordsResponseBody(Collections.emptyList(), null));

        List<String> fields = Arrays.asList("field1", "field2");
        assertThat(sut.getRecords(1, fields, "$id = 10")).isEmpty();
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        GetRecordsRequest expected =
                new GetRecordsRequest()
                        .setApp(1L)
                        .setFields(fields)
                        .setQuery("$id = 10")
                        .setTotalCount(false);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getRecords_GetRecordsRequest() {
        GetRecordsRequest req = new GetRecordsRequest();
        GetRecordsResponseBody resp = new GetRecordsResponseBody(Collections.emptyList(), null);
        mockClient.setResponseBody(resp);

        assertThat(sut.getRecords(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRecordsByCursor_String() {
        GetRecordsByCursorResponseBody responseBody =
                new GetRecordsByCursorResponseBody(true, Collections.emptyList());
        mockClient.setResponseBody(responseBody);

        assertThat(sut.getRecordsByCursor("cursor_id")).isEqualTo(responseBody);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS_BY_CURSOR);
        assertThat(mockClient.getLastBody()).isInstanceOf(GetRecordsByCursorRequest.class);
        GetRecordsByCursorRequest expected = new GetRecordsByCursorRequest().setId("cursor_id");
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getRecordsByCursor_GetRecordsByCursorRequest() {
        GetRecordsByCursorRequest req = new GetRecordsByCursorRequest();
        GetRecordsByCursorResponseBody resp =
                new GetRecordsByCursorResponseBody(false, Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.getRecordsByCursor(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORDS_BY_CURSOR);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateRecord_long_long_Record() {
        mockClient.setResponseBody(new UpdateRecordResponseBody(2));

        assertThat(sut.updateRecord(1, 100, new Record())).isEqualTo(2L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD);
        assertThat(mockClient.getLastBody()).isInstanceOf(UpdateRecordRequest.class);
        UpdateRecordRequest expected =
                new UpdateRecordRequest()
                        .setApp(1L)
                        .setId(100L)
                        .setRevision(null)
                        .setUpdateKey(null)
                        .setRecord(new Record());
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateRecord_long_UpdateKey_Record() {
        mockClient.setResponseBody(new UpdateRecordResponseBody(2));

        UpdateKey key = new UpdateKey("text", "test");
        assertThat(sut.updateRecord(1, key, new Record())).isEqualTo(2L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD);
        assertThat(mockClient.getLastBody()).isInstanceOf(UpdateRecordRequest.class);
        UpdateRecordRequest expected =
                new UpdateRecordRequest()
                        .setApp(1L)
                        .setId(null)
                        .setRevision(null)
                        .setUpdateKey(key)
                        .setRecord(new Record());
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateRecord_UpdateRecordRequest() {
        UpdateRecordRequest req = new UpdateRecordRequest();
        UpdateRecordResponseBody resp = new UpdateRecordResponseBody(2);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateRecord(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateRecordAssignees_long_long_List() {
        mockClient.setResponseBody(new UpdateRecordAssigneesResponseBody(15L));

        List<String> assignees = Lists.newArrayList("user1", "user2");
        assertThat(sut.updateRecordAssignees(7L, 2L, assignees)).isEqualTo(15L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_ASSIGNEES);
        assertThat(mockClient.getLastBody()).isInstanceOf(UpdateRecordAssigneesRequest.class);
        UpdateRecordAssigneesRequest expected =
                new UpdateRecordAssigneesRequest().setApp(7L).setId(2L).setAssignees(assignees);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateRecordAssignees_UpdateRecordAssigneesRequest() {
        UpdateRecordAssigneesRequest req = new UpdateRecordAssigneesRequest();
        UpdateRecordAssigneesResponseBody resp = new UpdateRecordAssigneesResponseBody(2);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateRecordAssignees(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_ASSIGNEES);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateRecords_long_List() {
        RecordForUpdate up1 = new RecordForUpdate(1L, new Record());
        RecordForUpdate up2 = new RecordForUpdate(new UpdateKey("text", "test"), new Record());
        RecordUpdateResult result1 = new RecordUpdateResult(1, 2, RecordOperationType.UPDATE);
        RecordUpdateResult result2 = new RecordUpdateResult(100, 2, RecordOperationType.INSERT);
        mockClient.setResponseBody(new UpdateRecordsResponseBody(Arrays.asList(result1, result2)));

        List<RecordForUpdate> records = Arrays.asList(up1, up2);
        List<RecordUpdateResult> result = sut.updateRecords(1, records);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getRevision()).isEqualTo(2);
        assertThat(result.get(0).getOperation()).isEqualTo(RecordOperationType.UPDATE);
        assertThat(result.get(1).getId()).isEqualTo(100);
        assertThat(result.get(1).getRevision()).isEqualTo(2);
        assertThat(result.get(1).getOperation()).isEqualTo(RecordOperationType.INSERT);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORDS);
        assertThat(mockClient.getLastBody()).isInstanceOf(UpdateRecordsRequest.class);
        UpdateRecordsRequest expected = new UpdateRecordsRequest().setApp(1L).setRecords(records);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateRecords_UpdateRecordsRequest() {
        UpdateRecordsRequest req = new UpdateRecordsRequest();
        UpdateRecordsResponseBody resp = new UpdateRecordsResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.updateRecords(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateRecordStatus_long_long_String() {
        mockClient.setResponseBody(new UpdateRecordStatusResponseBody(10L));

        assertThat(sut.updateRecordStatus(8L, 13L, "action")).isEqualTo(10L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_STATUS);
        assertThat(mockClient.getLastBody()).isInstanceOf(UpdateRecordStatusRequest.class);
        UpdateRecordStatusRequest expected =
                new UpdateRecordStatusRequest().setApp(8L).setId(13L).setAction("action");
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateRecordStatus_long_long_String_String() {
        mockClient.setResponseBody(new UpdateRecordStatusResponseBody(10L));

        assertThat(sut.updateRecordStatus(18L, 29L, "action", "user")).isEqualTo(10L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_STATUS);
        assertThat(mockClient.getLastBody()).isInstanceOf(UpdateRecordStatusRequest.class);
        UpdateRecordStatusRequest expected =
                new UpdateRecordStatusRequest()
                        .setApp(18L)
                        .setId(29L)
                        .setAction("action")
                        .setAssignee("user");
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateRecordStatus_UpdateRecordStatusRequest() {
        UpdateRecordStatusRequest req = new UpdateRecordStatusRequest();
        UpdateRecordStatusResponseBody resp = new UpdateRecordStatusResponseBody(2);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateRecordStatus(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_STATUS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateRecordStatuses_long_List() {
        mockClient.setResponseBody(new UpdateRecordStatusesResponseBody(Collections.emptyList()));

        StatusAction status = new StatusAction().setId(24L).setAction("action");
        assertThat(sut.updateRecordStatuses(10L, Lists.newArrayList(status)))
                .isEqualTo(Collections.emptyList());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_STATUSES);
        assertThat(mockClient.getLastBody()).isInstanceOf(UpdateRecordStatusesRequest.class);
        UpdateRecordStatusesRequest expected =
                new UpdateRecordStatusesRequest().setApp(10L).setRecords(Lists.newArrayList(status));
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateRecordStatuses_UpdateRecordStatusesRequest() {
        UpdateRecordStatusesRequest req = new UpdateRecordStatusesRequest();
        UpdateRecordStatusesResponseBody resp =
                new UpdateRecordStatusesResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.updateRecordStatuses(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_STATUSES);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }
}
