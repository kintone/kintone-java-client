package com.kintone.client;

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
import com.kintone.client.model.record.PostedRecordComment;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.RecordComment;
import com.kintone.client.model.record.RecordForUpdate;
import com.kintone.client.model.record.RecordRevision;
import com.kintone.client.model.record.StatusAction;
import com.kintone.client.model.record.UpdateKey;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/** A client that operates record APIs. */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RecordClient {

    private static final long MAX_OFFSET = 10000;

    private final InternalClient client;
    private final List<ResponseHandler> handlers;

    /**
     * Adds 1 record to an App.
     *
     * @param app the App ID
     * @param record the record object. See {@link Record}
     * @return the record ID of the created record
     */
    public long addRecord(long app, Record record) {
        AddRecordRequest req = new AddRecordRequest();
        req.setApp(app);
        req.setRecord(record);
        return addRecord(req).getId();
    }

    /**
     * Adds 1 record to an App.
     *
     * @param request the request parameters. See {@link AddRecordRequest}
     * @return the response data. See {@link AddRecordResponseBody}
     */
    public AddRecordResponseBody addRecord(AddRecordRequest request) {
        return client.call(KintoneApi.ADD_RECORD, request, handlers);
    }

    /**
     * Add a comment to a record in an app.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @param comment an object including comment details
     * @return the comment ID
     */
    public long addRecordComment(long app, long recordId, RecordComment comment) {
        AddRecordCommentRequest req = new AddRecordCommentRequest();
        req.setApp(app);
        req.setRecord(recordId);
        req.setComment(comment);
        return addRecordComment(req).getId();
    }

    /**
     * Add a comment to a record in an app.
     *
     * @param request the request parameters. See {@link AddRecordCommentRequest}
     * @return the response data. See {@link AddRecordCommentResponseBody}
     */
    public AddRecordCommentResponseBody addRecordComment(AddRecordCommentRequest request) {
        return client.call(KintoneApi.ADD_RECORD_COMMENT, request, handlers);
    }

    /**
     * Adds multiple records to an App.
     *
     * @param app the App ID
     * @param records a list of records. See {@link Record}
     * @return the record IDs of the created records
     */
    public List<Long> addRecords(long app, List<Record> records) {
        AddRecordsRequest req = new AddRecordsRequest();
        req.setApp(app);
        req.setRecords(records);
        return addRecords(req).getIds();
    }

    /**
     * Adds multiple records to an App.
     *
     * @param request the request parameters. See {@link AddRecordsRequest}
     * @return the response data. See {@link AddRecordsResponseBody}
     */
    public AddRecordsResponseBody addRecords(AddRecordsRequest request) {
        return client.call(KintoneApi.ADD_RECORDS, request, handlers);
    }

    /**
     * Get the number of records.
     *
     * @param app the App ID
     * @return the number of records
     */
    public long countRecords(long app) {
        return countRecords(app, null);
    }

    /**
     * Get the number of records.
     *
     * @param app the App ID
     * @param query the query string
     * @return the number of records
     */
    public long countRecords(long app, String query) {
        GetRecordsRequest req = new GetRecordsRequest();
        req.setApp(app);
        req.setQuery(query);
        req.setTotalCount(true);
        return getRecords(req).getTotalCount();
    }

    /**
     * Creates a cursor so that large amount of records can be obtained from an App.
     *
     * @param app the App ID
     * @return the created cursor ID
     */
    public String createCursor(long app) {
        return createCursor(app, null, null);
    }

    /**
     * Creates a cursor so that large amount of records can be obtained from an App.
     *
     * @param app the App ID
     * @param fields the field codes to be included in the response when using the Get Cursor API
     * @param query the query string that will specify what records will be responded when using the
     *     Get Cursor API
     * @return the created cursor ID
     */
    public String createCursor(long app, List<String> fields, String query) {
        CreateCursorRequest req = new CreateCursorRequest();
        req.setApp(app);
        req.setFields(fields);
        req.setQuery(query);
        return createCursor(req).getId();
    }

    /**
     * Creates a cursor so that large amount of records can be obtained from an App.
     *
     * @param request the request parameters. See {@link CreateCursorRequest}
     * @return the response data. See {@link CreateCursorResponseBody}
     */
    public CreateCursorResponseBody createCursor(CreateCursorRequest request) {
        return client.call(KintoneApi.CREATE_CURSOR, request, handlers);
    }

    /**
     * Deletes a cursor by specifying the cursor ID.
     *
     * @param id the cursor ID
     */
    public void deleteCursor(String id) {
        DeleteCursorRequest req = new DeleteCursorRequest();
        req.setId(id);
        deleteCursor(req);
    }

    /**
     * Deletes a cursor by specifying the cursor ID.
     *
     * @param request the request parameters. See {@link DeleteCursorRequest}
     * @return the response data. See {@link DeleteCursorResponseBody}
     */
    public DeleteCursorResponseBody deleteCursor(DeleteCursorRequest request) {
        return client.call(KintoneApi.DELETE_CURSOR, request, handlers);
    }

    /**
     * Delete a comment in a record in an app.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @param commentId the comment ID
     */
    public void deleteRecordComment(long app, long recordId, long commentId) {
        DeleteRecordCommentRequest req = new DeleteRecordCommentRequest();
        req.setApp(app);
        req.setRecord(recordId);
        req.setComment(commentId);
        deleteRecordComment(req);
    }

    /**
     * Delete a comment in a record in an app.
     *
     * @param request the request parameters. See {@link DeleteRecordCommentRequest}
     * @return the response data. See {@link DeleteRecordCommentResponseBody}
     */
    public DeleteRecordCommentResponseBody deleteRecordComment(DeleteRecordCommentRequest request) {
        return client.call(KintoneApi.DELETE_RECORD_COMMENT, request, handlers);
    }

    /**
     * Deletes multiple records in an app.
     *
     * @param app the app ID
     * @param recordIds a list of record IDs that will be deleted
     */
    public void deleteRecords(long app, List<Long> recordIds) {
        deleteRecords(app, recordIds, null);
    }

    /**
     * Deletes multiple records in an app.
     *
     * @param app the app ID
     * @param recordIds a list of record IDs that will be deleted
     * @param revisions the expected revision numbers. The first id number will correspond to the
     *     first revision number in the list, the second id to the second revision number, and so on.
     */
    public void deleteRecords(long app, List<Long> recordIds, List<Long> revisions) {
        DeleteRecordsRequest req = new DeleteRecordsRequest();
        req.setApp(app);
        req.setIds(recordIds);
        req.setRevisions(revisions);
        deleteRecords(req);
    }

    /**
     * Deletes multiple records in an app.
     *
     * @param request the request parameters. See {@link DeleteRecordsRequest}
     * @return the response data. See {@link DeleteRecordsResponseBody}
     */
    public DeleteRecordsResponseBody deleteRecords(DeleteRecordsRequest request) {
        return client.call(KintoneApi.DELETE_RECORDS, request, handlers);
    }

    /**
     * Retrieves details of 1 record from an App by specifying the App ID and record ID.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @return the record object. See {@link Record}
     */
    public Record getRecord(long app, long recordId) {
        GetRecordRequest req = new GetRecordRequest();
        req.setApp(app);
        req.setId(recordId);
        return getRecord(req).getRecord();
    }

    /**
     * Retrieves details of 1 record from an App by specifying the App ID and record ID.
     *
     * @param request the request parameters. See {@link GetRecordRequest}
     * @return the response data. See {@link GetRecordResponseBody}
     */
    public GetRecordResponseBody getRecord(GetRecordRequest request) {
        return client.call(KintoneApi.GET_RECORD, request, handlers);
    }

    /**
     * Retrieves multiple comments from a record in an app.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @return a list of comments
     */
    public List<PostedRecordComment> getRecordComments(long app, long recordId) {
        return getRecordComments(app, recordId, null, null, null);
    }

    /**
     * Retrieves multiple comments from a record in an app.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @param order the sort order of the Comment ID. Specifying {@link Order#ASC} will sort the
     *     comments in ascending order, and {@link Order#DESC} will sort the comments in descending
     *     order.
     * @param offset This skips the retrieval of the first number of comments.
     * @param limit the number of records to retrieve
     * @return a list of comments. See {@link PostedRecordComment}
     */
    public List<PostedRecordComment> getRecordComments(
            long app, long recordId, Order order, Long offset, Long limit) {
        GetRecordCommentsRequest req = new GetRecordCommentsRequest();
        req.setApp(app);
        req.setRecord(recordId);
        req.setOrder(order);
        req.setOffset(offset);
        req.setLimit(limit);
        return getRecordComments(req).getComments();
    }

    /**
     * Retrieves multiple comments from a record in an app.
     *
     * @param request the request parameters. See {@link GetRecordCommentsRequest}
     * @return the response data. See {@link GetRecordCommentsResponseBody}
     */
    public GetRecordCommentsResponseBody getRecordComments(GetRecordCommentsRequest request) {
        return client.call(KintoneApi.GET_RECORD_COMMENTS, request, handlers);
    }

    /**
     * Retrieves details of multiple records from an App.
     *
     * @param app the App ID
     * @return a list of records. See {@link Record}
     */
    public List<Record> getRecords(long app) {
        return getRecords(app, null, null);
    }

    /**
     * Retrieves details of multiple records from an App.
     *
     * @param app the App ID
     * @param limit the maximum number of records to be retrieved
     * @param offset This skips retrieving the first number of records.
     * @return a list of records. See {@link Record}
     */
    public List<Record> getRecords(long app, int limit, long offset) {
        return getRecords(app, null, limit, offset);
    }

    /**
     * Retrieves details of multiple records from an App.
     *
     * @param app the App ID
     * @param fields the field codes to be included in the response. Ignoring this parameter will
     *     return all accessible fields that exist in the App.
     * @param limit the maximum number of records to be retrieved
     * @param offset This skips retrieving the first number of records.
     * @return a list of records. See {@link Record}
     */
    public List<Record> getRecords(long app, List<String> fields, int limit, long offset) {
        if (offset > MAX_OFFSET) {
            throw new IllegalArgumentException(
                    "The offset parameter is too large. Use the cursor API instead.");
        }
        return getRecords(app, fields, "limit " + limit + " offset " + offset);
    }

    /**
     * Retrieves details of multiple records from an App by specifying a query string.
     *
     * @param app the App ID
     * @param query the query string that specifies what records to include in the response
     * @return a list of records. See {@link Record}
     */
    public List<Record> getRecords(long app, String query) {
        return getRecords(app, null, query);
    }

    /**
     * Retrieves details of multiple records from an App by specifying a query string.
     *
     * @param app the App ID
     * @param fields the field codes to be included in the response. Ignoring this parameter will
     *     return all accessible fields that exist in the App.
     * @param query the query string that specifies what records to include in the response
     * @return a list of records. See {@link Record}
     */
    public List<Record> getRecords(long app, List<String> fields, String query) {
        GetRecordsRequest req = new GetRecordsRequest();
        req.setApp(app);
        req.setFields(fields);
        req.setQuery(query);
        req.setTotalCount(false);
        return getRecords(req).getRecords();
    }

    /**
     * Retrieves details of multiple records from an App.
     *
     * @param request the request parameters. See {@link GetRecordsRequest}
     * @return the response data. See {@link GetRecordsResponseBody}
     */
    public GetRecordsResponseBody getRecords(GetRecordsRequest request) {
        return client.call(KintoneApi.GET_RECORDS, request, handlers);
    }

    /**
     * Retrieves multiple records from an App by specifying the cursor ID.
     *
     * @param id the cursor ID
     * @return the response data. See {@link GetRecordsByCursorResponseBody}
     */
    public GetRecordsByCursorResponseBody getRecordsByCursor(String id) {
        GetRecordsByCursorRequest req = new GetRecordsByCursorRequest();
        req.setId(id);
        return getRecordsByCursor(req);
    }

    /**
     * Retrieves multiple records from an App by specifying the cursor ID.
     *
     * @param request the request parameters. See {@link GetRecordsByCursorRequest}
     * @return the response data. See {@link GetRecordsByCursorResponseBody}
     */
    public GetRecordsByCursorResponseBody getRecordsByCursor(GetRecordsByCursorRequest request) {
        return client.call(KintoneApi.GET_RECORDS_BY_CURSOR, request, handlers);
    }

    /**
     * Updates details of 1 record in an App by specifying its record number.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @param record the record object. See {@link Record}
     * @return the revision number of the record
     */
    public long updateRecord(long app, long recordId, Record record) {
        UpdateRecordRequest req = new UpdateRecordRequest();
        req.setApp(app);
        req.setId(recordId);
        req.setRecord(record);
        return updateRecord(req).getRevision();
    }

    /**
     * Updates details of 1 record in an App by specifying a different unique key.
     *
     * @param app the App ID
     * @param updateKey the unique key of the record to be updated
     * @param record the record object. See {@link Record}
     * @return the revision number of the record
     */
    public long updateRecord(long app, UpdateKey updateKey, Record record) {
        UpdateRecordRequest req = new UpdateRecordRequest();
        req.setApp(app);
        req.setRecord(record);
        req.setUpdateKey(updateKey);
        return updateRecord(req).getRevision();
    }

    /**
     * Updates details of 1 record in an App by specifying its record number, or a different unique
     * key.
     *
     * @param request the request parameters. See {@link UpdateRecordRequest}
     * @return the response data. See {@link UpdateRecordResponseBody}
     */
    public UpdateRecordResponseBody updateRecord(UpdateRecordRequest request) {
        return client.call(KintoneApi.UPDATE_RECORD, request, handlers);
    }

    /**
     * Updates the Assignees of a record status, that was set with the Process Management feature.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @param assignees the user codes of the Assignee(s)
     * @return the revision number of the record after updating the Assignees
     */
    public long updateRecordAssignees(long app, long recordId, List<String> assignees) {
        UpdateRecordAssigneesRequest req = new UpdateRecordAssigneesRequest();
        req.setApp(app);
        req.setId(recordId);
        req.setAssignees(assignees);
        return updateRecordAssignees(req).getRevision();
    }

    /**
     * Updates the Assignees of a record status, that was set with the Process Management feature.
     *
     * @param request the request parameters. See {@link UpdateRecordAssigneesRequest}
     * @return the response data. See {@link UpdateRecordAssigneesResponseBody}
     */
    public UpdateRecordAssigneesResponseBody updateRecordAssignees(
            UpdateRecordAssigneesRequest request) {
        return client.call(KintoneApi.UPDATE_RECORD_ASSIGNEES, request, handlers);
    }

    /**
     * Updates details of multiple records in an App, by specifying their record numbers, or their
     * unique keys.
     *
     * @param app the App ID
     * @param records a list of objects that include id/updateKey, revision and record objects
     * @return a list of record revisions. See {@link RecordRevision}
     */
    public List<RecordRevision> updateRecords(long app, List<RecordForUpdate> records) {
        UpdateRecordsRequest req = new UpdateRecordsRequest();
        req.setApp(app);
        req.setRecords(records);
        return updateRecords(req).getRecords();
    }

    /**
     * Updates details of multiple records in an App.
     *
     * @param request the request parameters. See {@link UpdateRecordsRequest}
     * @return the response data. See {@link UpdateRecordsResponseBody}
     */
    public UpdateRecordsResponseBody updateRecords(UpdateRecordsRequest request) {
        return client.call(KintoneApi.UPDATE_RECORDS, request, handlers);
    }

    /**
     * Updates the Status of a record of an App, that was set with the Process Management feature.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @param action the Action name of the action to run
     * @return the revision number of the record after updating the status
     */
    public long updateRecordStatus(long app, long recordId, String action) {
        return updateRecordStatus(app, recordId, action, null);
    }

    /**
     * Updates the Status of a record of an App, that was set with the Process Management feature.
     *
     * @param app the App ID
     * @param recordId the record ID
     * @param action the Action name of the action to run
     * @param assignee the next Assignee. Specify the Assignee's code.
     * @return the revision number of the record after updating the status
     */
    public long updateRecordStatus(long app, long recordId, String action, String assignee) {
        UpdateRecordStatusRequest req = new UpdateRecordStatusRequest();
        req.setApp(app);
        req.setId(recordId);
        req.setAction(action);
        req.setAssignee(assignee);
        return updateRecordStatus(req).getRevision();
    }

    /**
     * Updates the Status of a record of an App, that was set with the Process Management feature.
     *
     * @param request the request parameters. See {@link UpdateRecordStatusRequest}
     * @return the response data. See {@link UpdateRecordStatusResponseBody}
     */
    public UpdateRecordStatusResponseBody updateRecordStatus(UpdateRecordStatusRequest request) {
        return client.call(KintoneApi.UPDATE_RECORD_STATUS, request, handlers);
    }

    /**
     * Updates the Statuses of multiple records of an App, that were set with the Process Management
     * feature.
     *
     * @param app the App ID
     * @param statusActions a list including information of the status actions to proceed
     * @return a list of record revisions. See {@link RecordRevision}
     */
    public List<RecordRevision> updateRecordStatuses(long app, List<StatusAction> statusActions) {
        UpdateRecordStatusesRequest req = new UpdateRecordStatusesRequest();
        req.setApp(app);
        req.setRecords(statusActions);
        return updateRecordStatuses(req).getRecords();
    }

    /**
     * Updates the Statuses of multiple records of an App, that were set with the Process Management
     * feature.
     *
     * @param request the request parameters. See {@link UpdateRecordStatusesRequest}
     * @return the response data. See {@link UpdateRecordStatusesResponseBody}
     */
    public UpdateRecordStatusesResponseBody updateRecordStatuses(
            UpdateRecordStatusesRequest request) {
        return client.call(KintoneApi.UPDATE_RECORD_STATUSES, request, handlers);
    }
}
