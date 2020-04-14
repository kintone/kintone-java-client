package com.kintone.client.model.app.field;

/** The display format of "Calculated Field" values. */
public enum DisplayFormat {

    /** Number. e.g "1000" */
    NUMBER,

    /** Number with thousands separator. e.g "1,000" */
    NUMBER_DIGIT,

    /** Date and time. e.g "Aug 06, 2012 2:03 PM" */
    DATETIME,

    /** Date. e.g "Aug 06, 2012" */
    DATE,

    /** Time. e.g "2:03 PM" */
    TIME,

    /** Hours and minutes. e.g. "29 hours 47 minutes" */
    HOUR_MINUTE,

    /** Days, hours and minutes. e.g. "1 day 5 hours 47 minutes" */
    DAY_HOUR_MINUTE
}
