package com.kintone.client.model.app.field;

import lombok.Data;

/** An object containing data of a related App. */
@Data
public class RelatedApp {

    /** The App ID of the related App. */
    private Long app;

    /** The App Code of the related App. */
    private String code;
}
