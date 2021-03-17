package com.kintone.client.model.app;

import lombok.Data;

/** An object representing the settings of reminder timing. */
@Data
public class ReminderTiming {

    /** The field code of the field used to determine the Reminder notification's timing. */
    private String code;

    /**
    * The number of days after the {@link #code} filed value (date or datetime) when the Reminder
    * notification is sent. A negative value indicates the number of days before the {@link #code}
    * filed value.
    */
    private Integer daysLater;

    /**
    * The number of hours after the {@link #code} filed value (datetime), shifted by daysLater, when
    * the Reminder notification is sent. A negative value indicates the number of hours before the
    * {@link #code} field value, shifted by daysLater.
    *
    * <p>In the response of get reminder notification settings API, this parameter is returned only
    * if the {@link #code} parameter is set to a Date and Time field and the "When" hours
    * before/after option is configured (instead of the "At" time option).
    */
    private Integer hoursLater;

    /**
    * The time when the Reminder notification is sent.
    *
    * <p>In the response of get reminder notification settings API, this parameter is returned if the
    * {@link #code} parameter is set to a date field or the "At" time option is configured.
    */
    private String time;
}
