package com.kintone.client.model.app;

import lombok.Data;

/** An object containing settings of record title. */
@Data
public class TitleFiled {

    /** The selection mode of the title field. */
    private TitleFieldSelectionMode selectionMode;

    /** The field to be used as the record title when the "selectionMode" is set to "MANUAL". */
    private String code;
}
