package com.kintone.client.model.app.field;

import lombok.Data;

/** Settings of an option of Checkbox, Drop-down, Multi-choice and Radio button field. */
@Data
public class Option {

    /** The display name of the option. */
    private String label;

    /** The display order (ascending) of the option, when listed with the other options. */
    private Long index;
}
