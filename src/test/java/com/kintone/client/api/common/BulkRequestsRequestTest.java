package com.kintone.client.api.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.KintoneApi;
import com.kintone.client.api.record.AddRecordRequest;
import com.kintone.client.api.record.AddRecordsRequest;
import com.kintone.client.api.record.DeleteRecordsRequest;
import com.kintone.client.api.record.UpdateRecordAssigneesRequest;
import com.kintone.client.api.record.UpdateRecordRequest;
import com.kintone.client.api.record.UpdateRecordStatusRequest;
import com.kintone.client.api.record.UpdateRecordStatusesRequest;
import com.kintone.client.api.record.UpdateRecordsRequest;
import com.kintone.client.model.BulkRequestContent;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BulkRequestsRequestTest {
    @Test
    public void bulkRequestsTest() {
        BulkRequestsRequest sut = new BulkRequestsRequest();

        assertThat(sut.getRequests()).isEmpty();

        AddRecordRequest addRecordRequest = new AddRecordRequest().setApp(6L);
        AddRecordsRequest addRecordsRequest = new AddRecordsRequest().setApp(3L);
        DeleteRecordsRequest deleteRecordsRequest = new DeleteRecordsRequest().setApp(10L);
        UpdateRecordAssigneesRequest updateRecordAssigneesRequest =
                new UpdateRecordAssigneesRequest().setApp(12L);
        UpdateRecordRequest updateRecordRequest = new UpdateRecordRequest().setApp(17L);
        UpdateRecordsRequest updateRecordsRequest = new UpdateRecordsRequest().setApp(18L);
        UpdateRecordStatusRequest updateRecordStatusRequest =
                new UpdateRecordStatusRequest().setApp(21L);
        UpdateRecordStatusesRequest updateRecordStatusesRequest =
                new UpdateRecordStatusesRequest().setApp(27L);

        sut.registerAddRecord(addRecordRequest);
        sut.registerAddRecords(addRecordsRequest);
        sut.registerDeleteRecords(deleteRecordsRequest);
        sut.registerUpdateRecordAssignees(updateRecordAssigneesRequest);
        sut.registerUpdateRecord(updateRecordRequest);
        sut.registerUpdateRecords(updateRecordsRequest);
        sut.registerUpdateRecordStatus(updateRecordStatusRequest);
        sut.registerUpdateRecordStatuses(updateRecordStatusesRequest);

        List<BulkRequestContent> requests = sut.getRequests();
        assertThat(requests).hasSize(8);

        assertThat(requests.get(0).getApi()).isEqualTo(KintoneApi.ADD_RECORD);
        assertThat(requests.get(0).getPayload()).isEqualTo(addRecordRequest);

        assertThat(requests.get(1).getApi()).isEqualTo(KintoneApi.ADD_RECORDS);
        assertThat(requests.get(1).getPayload()).isEqualTo(addRecordsRequest);

        assertThat(requests.get(2).getApi()).isEqualTo(KintoneApi.DELETE_RECORDS);
        assertThat(requests.get(2).getPayload()).isEqualTo(deleteRecordsRequest);

        assertThat(requests.get(3).getApi()).isEqualTo(KintoneApi.UPDATE_RECORD_ASSIGNEES);
        assertThat(requests.get(3).getPayload()).isEqualTo(updateRecordAssigneesRequest);

        assertThat(requests.get(4).getApi()).isEqualTo(KintoneApi.UPDATE_RECORD);
        assertThat(requests.get(4).getPayload()).isEqualTo(updateRecordRequest);

        assertThat(requests.get(5).getApi()).isEqualTo(KintoneApi.UPDATE_RECORDS);
        assertThat(requests.get(5).getPayload()).isEqualTo(updateRecordsRequest);

        assertThat(requests.get(6).getApi()).isEqualTo(KintoneApi.UPDATE_RECORD_STATUS);
        assertThat(requests.get(6).getPayload()).isEqualTo(updateRecordStatusRequest);

        assertThat(requests.get(7).getApi()).isEqualTo(KintoneApi.UPDATE_RECORD_STATUSES);
        assertThat(requests.get(7).getPayload()).isEqualTo(updateRecordStatusesRequest);
    }
}
