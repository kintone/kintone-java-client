package com.kintone.client.model.app.report;

/** An enum for representing the months when the quarterly Periodic Report will be generated. */
public enum QuarterlyPattern {

    /** Generates Periodic Report in January, April, July, October */
    JAN_APR_JUL_OCT,

    /** Generates Periodic Report in February, May, August, November */
    FEB_MAY_AUG_NOV,

    /** Generates Periodic Report in March, June, September, December */
    MAR_JUN_SEP_DEC
}
