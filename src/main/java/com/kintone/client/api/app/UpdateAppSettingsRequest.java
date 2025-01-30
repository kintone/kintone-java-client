package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.AppIcon;
import com.kintone.client.model.app.NumberPrecision;
import com.kintone.client.model.app.TitleFiled;
import lombok.Data;

/** A request object for Update App Settings API. */
@Data
public class UpdateAppSettingsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The App name (optional). If set to null, leaves this setting unchanged. */
    private String name;

    /** The App description (optional). If set to null, leaves this setting unchanged. */
    private String description;

    /**
     * An object containing information of the App icon (optional). If set to null, leaves this
     * setting unchanged.
     */
    private AppIcon icon;

    /** The color theme (optional). If set to null, leaves this setting unchanged. */
    private String theme;

    /**
     * An object containing settings of record title (optional). If set to null, leaves this setting
     * unchanged.
     */
    private TitleFiled titleField;

    /**
     * An object containing the precision settings of numbers and calculations (optional). If set to
     * null, leaves this setting unchanged.
     */
    private NumberPrecision numberPrecision;

    /**
     * The settings of first month of fiscal year (optional). If set to null, leaves this setting
     * unchanged.
     */
    private Integer firstMonthOfFiscalYear;

    /**
     * The on/off settings to show thumbnails of image files attached to the File fields (optional).
     * If set to null, leaves this setting unchanged.
     */
    private Boolean enableThumbnails;

    /**
     * The on/off settings of bulk deletion of records (optional). If set to null, leaves this setting
     * unchanged.
     */
    private Boolean enableBulkDeletion;

    /**
     * The on/off settings of record comments feature (optional). If set to null, leaves this setting
     * unchanged.
     */
    private Boolean enableComments;

    /**
     * The on/off settings to use the feature to "duplicate record" (optional). If set to null, leaves
     * this setting unchanged.
     */
    private Boolean enableDuplicateRecord;

    /**
     * The on/off settings of inline record editing feature (optional). If set to null, leaves this
     * setting unchanged.
     */
    private Boolean enableInlineRecordEditing;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;
}
