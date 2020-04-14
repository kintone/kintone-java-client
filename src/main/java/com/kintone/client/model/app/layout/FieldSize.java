package com.kintone.client.model.app.layout;

import lombok.Data;

/** The size settings of a field. */
@Data
public class FieldSize {

    /** The width of the field in pixels. */
    private Integer width;

    /** The height of the field in pixels, including the height of the field name. */
    private Integer height;

    /** The height of the field in pixels, excluding the height of the field name. */
    private Integer innerHeight;
}
