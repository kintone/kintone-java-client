package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.AppIcon;
import com.kintone.client.model.app.NumberPrecision;
import com.kintone.client.model.app.TitleFiled;
import lombok.Value;

/** A response object for Get App Settings Preview API. */
@Value
public class GetAppSettingsPreviewResponseBody implements KintoneResponseBody {

    /** The App name. */
    private final String name;

    /** The app description in HTML format. */
    private final String description;

    /** An object containing data of the App icon. */
    private final AppIcon icon;

    /** The color theme. */
    private final String theme;

    /** An object containing settings of record title. */
    private final TitleFiled titleField;

    /** An object containing the precision settings of numbers and calculations. */
    private final NumberPrecision numberPrecision;

    /** The settings of first month of fiscal year */
    private final int firstMonthOfFiscalYear;

    /** Thumbnails of image files attached to the File fields are enabled. */
    private final boolean enableThumbnails;

    /** The bulk deletion of records is enabled. */
    private final boolean enableBulkDeletion;

    /** The record comments feature is enabled. */
    private final boolean enableComments;

    /** The "duplicate record" feature is enabled. */
    private final boolean enableDuplicateRecord;

    /** The revision number of the App settings. */
    private final long revision;
}
