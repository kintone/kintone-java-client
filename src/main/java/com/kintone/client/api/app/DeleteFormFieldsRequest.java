package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Delete Form Fields API. */
@Data
public class DeleteFormFieldsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;

    /** The list of field codes of the fields to delete (required). */
    private List<String> fields;
}
