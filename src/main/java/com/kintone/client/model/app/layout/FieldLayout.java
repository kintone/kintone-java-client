package com.kintone.client.model.app.layout;

import com.kintone.client.model.record.FieldType;
import lombok.Data;

/** An object contains layout information of a form field. */
@Data
public class FieldLayout {

    /** The type of field. */
    private FieldType type;

    /** The field code. */
    private String code;

    /** The text set in the Label field. */
    private String label;

    /** The element ID of the Space field. */
    private String elementId;

    /** An object with data of the field's size. */
    private FieldSize size;
}
