package com.kintone.client.api.common;

import com.kintone.client.KintoneApi;
import com.kintone.client.api.KintoneRequest;
import com.kintone.client.api.record.AddRecordRequest;
import com.kintone.client.api.record.AddRecordsRequest;
import com.kintone.client.api.record.DeleteRecordsRequest;
import com.kintone.client.api.record.UpdateRecordAssigneesRequest;
import com.kintone.client.api.record.UpdateRecordRequest;
import com.kintone.client.api.record.UpdateRecordStatusRequest;
import com.kintone.client.api.record.UpdateRecordStatusesRequest;
import com.kintone.client.api.record.UpdateRecordsRequest;
import com.kintone.client.model.BulkRequestContent;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** A request object for Bulk Requests API. */
@Data
public class BulkRequestsRequest implements KintoneRequest {

    /** The list of requests. */
    private final List<BulkRequestContent> requests = new ArrayList<>();

    /**
     * Add a request of Add Record API.
     *
     * @param request a request object for Add Record API
     */
    public void registerAddRecord(AddRecordRequest request) {
        register(KintoneApi.ADD_RECORD, request);
    }

    /**
     * Add a request of Add Records API.
     *
     * @param request a request object for Add Records API
     */
    public void registerAddRecords(AddRecordsRequest request) {
        register(KintoneApi.ADD_RECORDS, request);
    }

    /**
     * Add a request of Update Record API.
     *
     * @param request a request object for Update Record API
     */
    public void registerUpdateRecord(UpdateRecordRequest request) {
        register(KintoneApi.UPDATE_RECORD, request);
    }

    /**
     * Add a request of Update Records API.
     *
     * @param request a request object for Update Records API
     */
    public void registerUpdateRecords(UpdateRecordsRequest request) {
        register(KintoneApi.UPDATE_RECORDS, request);
    }

    /**
     * Add a request of Delete Records API.
     *
     * @param request a request object for Delete Records API
     */
    public void registerDeleteRecords(DeleteRecordsRequest request) {
        register(KintoneApi.DELETE_RECORDS, request);
    }

    /**
     * Add a request of Update Record Status API.
     *
     * @param request a request object for Update Record Status API
     */
    public void registerUpdateRecordStatus(UpdateRecordStatusRequest request) {
        register(KintoneApi.UPDATE_RECORD_STATUS, request);
    }

    /**
     * Add a request of Update Record Statuses API.
     *
     * @param request a request object for Update Record Statuses API
     */
    public void registerUpdateRecordStatuses(UpdateRecordStatusesRequest request) {
        register(KintoneApi.UPDATE_RECORD_STATUSES, request);
    }

    /**
     * Add a request of Update Record Assignees API.
     *
     * @param request a request object for Update Record Assignees API
     */
    public void registerUpdateRecordAssignees(UpdateRecordAssigneesRequest request) {
        register(KintoneApi.UPDATE_RECORD_ASSIGNEES, request);
    }

    private void register(KintoneApi api, KintoneRequest request) {
        BulkRequestContent req = new BulkRequestContent(api, request);
        requests.add(req);
    }
}
