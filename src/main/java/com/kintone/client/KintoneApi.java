package com.kintone.client;

import static com.kintone.client.KintoneHttpMethod.*;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.api.app.AddAppResponseBody;
import com.kintone.client.api.app.AddFormFieldsResponseBody;
import com.kintone.client.api.app.DeleteFormFieldsResponseBody;
import com.kintone.client.api.app.DeployAppResponseBody;
import com.kintone.client.api.app.EvaluateRecordAclResponseBody;
import com.kintone.client.api.app.GetAppAclPreviewResponseBody;
import com.kintone.client.api.app.GetAppAclResponseBody;
import com.kintone.client.api.app.GetAppActionsPreviewResponseBody;
import com.kintone.client.api.app.GetAppActionsResponseBody;
import com.kintone.client.api.app.GetAppCustomizePreviewResponseBody;
import com.kintone.client.api.app.GetAppCustomizeResponseBody;
import com.kintone.client.api.app.GetAppResponseBody;
import com.kintone.client.api.app.GetAppSettingsPreviewResponseBody;
import com.kintone.client.api.app.GetAppSettingsResponseBody;
import com.kintone.client.api.app.GetAppsResponseBody;
import com.kintone.client.api.app.GetDeployStatusResponseBody;
import com.kintone.client.api.app.GetFieldAclPreviewResponseBody;
import com.kintone.client.api.app.GetFieldAclResponseBody;
import com.kintone.client.api.app.GetFormFieldsPreviewResponseBody;
import com.kintone.client.api.app.GetFormFieldsResponseBody;
import com.kintone.client.api.app.GetFormLayoutPreviewResponseBody;
import com.kintone.client.api.app.GetFormLayoutResponseBody;
import com.kintone.client.api.app.GetGeneralNotificationsPreviewResponseBody;
import com.kintone.client.api.app.GetGeneralNotificationsResponseBody;
import com.kintone.client.api.app.GetPerRecordNotificationsPreviewResponseBody;
import com.kintone.client.api.app.GetPerRecordNotificationsResponseBody;
import com.kintone.client.api.app.GetProcessManagementPreviewResponseBody;
import com.kintone.client.api.app.GetProcessManagementResponseBody;
import com.kintone.client.api.app.GetRecordAclPreviewResponseBody;
import com.kintone.client.api.app.GetRecordAclResponseBody;
import com.kintone.client.api.app.GetReminderNotificationsPreviewResponseBody;
import com.kintone.client.api.app.GetReminderNotificationsResponseBody;
import com.kintone.client.api.app.GetReportsPreviewResponseBody;
import com.kintone.client.api.app.GetReportsResponseBody;
import com.kintone.client.api.app.GetViewsPreviewResponseBody;
import com.kintone.client.api.app.GetViewsResponseBody;
import com.kintone.client.api.app.UpdateAppAclResponseBody;
import com.kintone.client.api.app.UpdateAppActionsResponseBody;
import com.kintone.client.api.app.UpdateAppCustomizeResponseBody;
import com.kintone.client.api.app.UpdateAppSettingsResponseBody;
import com.kintone.client.api.app.UpdateFieldAclResponseBody;
import com.kintone.client.api.app.UpdateFormFieldsResponseBody;
import com.kintone.client.api.app.UpdateFormLayoutResponseBody;
import com.kintone.client.api.app.UpdateGeneralNotificationsResponseBody;
import com.kintone.client.api.app.UpdatePerRecordNotificationsResponseBody;
import com.kintone.client.api.app.UpdateProcessManagementResponseBody;
import com.kintone.client.api.app.UpdateRecordAclResponseBody;
import com.kintone.client.api.app.UpdateReminderNotificationsResponseBody;
import com.kintone.client.api.app.UpdateReportsResponseBody;
import com.kintone.client.api.app.UpdateViewsResponseBody;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.api.record.AddRecordCommentResponseBody;
import com.kintone.client.api.record.AddRecordResponseBody;
import com.kintone.client.api.record.AddRecordsResponseBody;
import com.kintone.client.api.record.CreateCursorResponseBody;
import com.kintone.client.api.record.DeleteCursorResponseBody;
import com.kintone.client.api.record.DeleteRecordCommentResponseBody;
import com.kintone.client.api.record.DeleteRecordsResponseBody;
import com.kintone.client.api.record.GetRecordCommentsResponseBody;
import com.kintone.client.api.record.GetRecordResponseBody;
import com.kintone.client.api.record.GetRecordsByCursorResponseBody;
import com.kintone.client.api.record.GetRecordsResponseBody;
import com.kintone.client.api.record.UpdateRecordAssigneesResponseBody;
import com.kintone.client.api.record.UpdateRecordResponseBody;
import com.kintone.client.api.record.UpdateRecordStatusResponseBody;
import com.kintone.client.api.record.UpdateRecordStatusesResponseBody;
import com.kintone.client.api.record.UpdateRecordsResponseBody;
import com.kintone.client.api.schema.GetApiListResponseBody;
import com.kintone.client.api.space.AddGuestsResponseBody;
import com.kintone.client.api.space.AddSpaceFromTemplateResponseBody;
import com.kintone.client.api.space.AddThreadCommentResponseBody;
import com.kintone.client.api.space.DeleteGuestsResponseBody;
import com.kintone.client.api.space.DeleteSpaceResponseBody;
import com.kintone.client.api.space.GetSpaceMembersResponseBody;
import com.kintone.client.api.space.GetSpaceResponseBody;
import com.kintone.client.api.space.UpdateSpaceBodyResponseBody;
import com.kintone.client.api.space.UpdateSpaceGuestsResponseBody;
import com.kintone.client.api.space.UpdateSpaceMembersResponseBody;
import com.kintone.client.api.space.UpdateThreadResponseBody;
import lombok.AccessLevel;
import lombok.Getter;

/** An enum representing Kintone APIs. */
@Getter(AccessLevel.PACKAGE)
public enum KintoneApi {
    GET_RECORD(GET, "record", GetRecordResponseBody.class),
    ADD_RECORD(POST, "record", AddRecordResponseBody.class),
    UPDATE_RECORD(PUT, "record", UpdateRecordResponseBody.class),
    GET_RECORDS(GET, "records", GetRecordsResponseBody.class),
    ADD_RECORDS(POST, "records", AddRecordsResponseBody.class),
    UPDATE_RECORDS(PUT, "records", UpdateRecordsResponseBody.class),
    DELETE_RECORDS(DELETE, "records", DeleteRecordsResponseBody.class),
    CREATE_CURSOR(POST, "records/cursor", CreateCursorResponseBody.class),
    GET_RECORDS_BY_CURSOR(GET, "records/cursor", GetRecordsByCursorResponseBody.class),
    DELETE_CURSOR(DELETE, "records/cursor", DeleteCursorResponseBody.class),
    ADD_RECORD_COMMENT(POST, "record/comment", AddRecordCommentResponseBody.class),
    DELETE_RECORD_COMMENT(DELETE, "record/comment", DeleteRecordCommentResponseBody.class),
    GET_RECORD_COMMENTS(GET, "record/comments", GetRecordCommentsResponseBody.class),
    UPDATE_RECORD_ASSIGNEES(PUT, "record/assignees", UpdateRecordAssigneesResponseBody.class),
    UPDATE_RECORD_STATUS(PUT, "record/status", UpdateRecordStatusResponseBody.class),
    UPDATE_RECORD_STATUSES(PUT, "records/status", UpdateRecordStatusesResponseBody.class),
    UPLOAD_FILE(POST, "file", UploadFileResponseBody.class),
    DOWNLOAD_FILE(GET, "file", DownloadFileResponseBody.class),
    BULK_REQUESTS(POST, "bulkRequest", BulkRequestsResponseBody.class),
    GET_FORM_FIELDS(GET, "app/form/fields", GetFormFieldsResponseBody.class),
    GET_FORM_FIELDS_PREVIEW(GET, "preview/app/form/fields", GetFormFieldsPreviewResponseBody.class),
    ADD_FORM_FIELDS(POST, "preview/app/form/fields", AddFormFieldsResponseBody.class),
    UPDATE_FORM_FIELDS(PUT, "preview/app/form/fields", UpdateFormFieldsResponseBody.class),
    DELETE_FORM_FIELDS(DELETE, "preview/app/form/fields", DeleteFormFieldsResponseBody.class),
    GET_FORM_LAYOUT(GET, "app/form/layout", GetFormLayoutResponseBody.class),
    GET_FORM_LAYOUT_PREVIEW(GET, "preview/app/form/layout", GetFormLayoutPreviewResponseBody.class),
    UPDATE_FORM_LAYOUT(PUT, "preview/app/form/layout", UpdateFormLayoutResponseBody.class),
    GET_GENERAL_NOTIFICATIONS(
            GET, "app/notifications/general", GetGeneralNotificationsResponseBody.class),
    GET_GENERAL_NOTIFICATIONS_PREVIEW(
            GET, "preview/app/notifications/general", GetGeneralNotificationsPreviewResponseBody.class),
    UPDATE_GENERAL_NOTIFICATIONS(
            PUT, "preview/app/notifications/general", UpdateGeneralNotificationsResponseBody.class),
    GET_PRE_RECORD_NOTIFICATIONS(
            GET, "app/notifications/perRecord", GetPerRecordNotificationsResponseBody.class),
    GET_PRE_RECORD_NOTIFICATIONS_PREVIEW(
            GET,
            "preview/app/notifications/perRecord",
            GetPerRecordNotificationsPreviewResponseBody.class),
    UPDATE_PRE_RECORD_NOTIFICATIONS(
            PUT, "preview/app/notifications/perRecord", UpdatePerRecordNotificationsResponseBody.class),
    GET_REMINDER_NOTIFICATIONS(
            GET, "app/notifications/reminder", GetReminderNotificationsResponseBody.class),
    GET_REMINDER_NOTIFICATIONS_PREVIEW(
            GET, "preview/app/notifications/reminder", GetReminderNotificationsPreviewResponseBody.class),
    UPDATE_REMINDER_NOTIFICATIONS(
            PUT, "preview/app/notifications/reminder", UpdateReminderNotificationsResponseBody.class),
    GET_REPORTS(GET, "app/reports", GetReportsResponseBody.class),
    GET_REPORTS_PREVIEW(GET, "preview/app/reports", GetReportsPreviewResponseBody.class),
    UPDATE_REPORTS(PUT, "preview/app/reports", UpdateReportsResponseBody.class),
    GET_VIEWS(GET, "app/views", GetViewsResponseBody.class),
    GET_VIEWS_PREVIEW(GET, "preview/app/views", GetViewsPreviewResponseBody.class),
    UPDATE_VIEWS(PUT, "preview/app/views", UpdateViewsResponseBody.class),
    GET_APP(GET, "app", GetAppResponseBody.class),
    GET_APPS(GET, "apps", GetAppsResponseBody.class),
    ADD_APP(POST, "preview/app", AddAppResponseBody.class),
    GET_APP_SETTINGS(GET, "app/settings", GetAppSettingsResponseBody.class),
    GET_APP_SETTINGS_PREVIEW(GET, "preview/app/settings", GetAppSettingsPreviewResponseBody.class),
    UPDATE_APP_SETTINGS(PUT, "preview/app/settings", UpdateAppSettingsResponseBody.class),
    GET_PROCESS_MANAGEMENT(GET, "app/status", GetProcessManagementResponseBody.class),
    GET_PROCESS_MANAGEMENT_PREVIEW(
            GET, "preview/app/status", GetProcessManagementPreviewResponseBody.class),
    UPDATE_PROCESS_MANAGEMENT(PUT, "preview/app/status", UpdateProcessManagementResponseBody.class),
    GET_APP_ACTIONS(GET, "app/actions", GetAppActionsResponseBody.class),
    GET_APP_ACTIONS_PREVIEW(GET, "preview/app/actions", GetAppActionsPreviewResponseBody.class),
    UPDATE_APP_ACTIONS(PUT, "preview/app/actions", UpdateAppActionsResponseBody.class),
    GET_APP_CUSTOMIZE(GET, "app/customize", GetAppCustomizeResponseBody.class),
    GET_APP_CUSTOMIZE_PREVIEW(GET, "preview/app/customize", GetAppCustomizePreviewResponseBody.class),
    UPDATE_APP_CUSTOMIZE(PUT, "preview/app/customize", UpdateAppCustomizeResponseBody.class),
    GET_APP_ACL(GET, "app/acl", GetAppAclResponseBody.class),
    GET_APP_ACL_PREVIEW(GET, "preview/app/acl", GetAppAclPreviewResponseBody.class),
    UPDATE_APP_ACL(PUT, "preview/app/acl", UpdateAppAclResponseBody.class),
    GET_RECORD_ACL(GET, "record/acl", GetRecordAclResponseBody.class),
    GET_RECORD_ACL_PREVIEW(GET, "preview/record/acl", GetRecordAclPreviewResponseBody.class),
    UPDATE_RECORD_ACL(PUT, "preview/record/acl", UpdateRecordAclResponseBody.class),
    EVALUATE_RECORD_ACL(GET, "records/acl/evaluate", EvaluateRecordAclResponseBody.class),
    GET_FIELD_ACL(GET, "field/acl", GetFieldAclResponseBody.class),
    GET_FIELD_ACL_PREVIEW(GET, "preview/field/acl", GetFieldAclPreviewResponseBody.class),
    UPDATE_FIELD_ACL(PUT, "preview/field/acl", UpdateFieldAclResponseBody.class),
    GET_DEPLOY_STATUS(GET, "preview/app/deploy", GetDeployStatusResponseBody.class),
    DEPLOY_APP(POST, "preview/app/deploy", DeployAppResponseBody.class),
    GET_SPACE(GET, "space", GetSpaceResponseBody.class),
    DELETE_SPACE(DELETE, "space", DeleteSpaceResponseBody.class),
    ADD_SPACE_FROM_TEMPLATE(POST, "template/space", AddSpaceFromTemplateResponseBody.class),
    UPDATE_SPACE_BODY(PUT, "space/body", UpdateSpaceBodyResponseBody.class),
    UPDATE_THREAD(PUT, "space/thread", UpdateThreadResponseBody.class),
    ADD_THREAD_COMMENT(POST, "space/thread/comment", AddThreadCommentResponseBody.class),
    GET_SPACE_MEMBERS(GET, "space/members", GetSpaceMembersResponseBody.class),
    UPDATE_SPACE_MEMBERS(PUT, "space/members", UpdateSpaceMembersResponseBody.class),
    ADD_GUESTS(POST, "guests", AddGuestsResponseBody.class),
    DELETE_GUESTS(DELETE, "guests", DeleteGuestsResponseBody.class),
    UPDATE_SPACE_GUESTS(PUT, "space/guests", UpdateSpaceGuestsResponseBody.class),
    GET_API_LIST(GET, "apis", GetApiListResponseBody.class);

    private final KintoneHttpMethod method;
    private final String endpoint;
    private final Class<? extends KintoneResponseBody> responseClass;

    KintoneApi(
            KintoneHttpMethod method,
            String endpoint,
            Class<? extends KintoneResponseBody> responseClass) {
        this.method = method;
        this.endpoint = endpoint;
        this.responseClass = responseClass;
    }
}
