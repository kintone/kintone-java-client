package com.kintone.client.model.app.field;

import lombok.Data;

/** An object containing data of the "Datasource App" setting of a Related Records field. */
@Data
public class RelatedApp {

    /** The App ID of the Datasource App. */
    private Long app;

    /** The App Code of the Datasource App. */
    private String code;
}
