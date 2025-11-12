package com.kintone.client.model.app;

import com.kintone.client.model.User;
import java.time.ZonedDateTime;
import lombok.Value;

/** Usage statistics information of an App retrieved by Get Apps Statistics API. */
@Value
public class AppStatistics {

    /** The App ID. */
    private final Long id;

    /** The name of the App. */
    private final String name;

    /**
     * If the App was created inside a Space, it will return the Space information. If not, null is
     * returned.
     */
    private final AppSpace space;

    /** The App group classification. */
    private final String appGroup;

    /** The status of the App settings. */
    private final AppStatisticsStatus status;

    /** The date and time when the record was last updated. */
    private final ZonedDateTime recordUpdatedAt;

    /** The total number of records. */
    private final Long recordCount;

    /** The total number of fields. */
    private final Long fieldCount;

    /** The number of API calls per day. */
    private final Long dailyRequestCount;

    /** The file storage usage in bytes. */
    private final Long storageUsage;

    /** Whether the App has JavaScript/CSS/plugin customizations. */
    private final Boolean customized;

    /** The information of the user who created the App. */
    private final User creator;

    /** The date of when the App was created. */
    private final ZonedDateTime createdAt;

    /** The information of the user who last updated the App settings. */
    private final User modifier;

    /** The date of when the App settings were last modified. */
    private final ZonedDateTime modifiedAt;
}
