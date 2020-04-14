package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Update Guest Members API. */
@Data
public class UpdateSpaceGuestsRequest implements KintoneRequest {

    /** The Guest Space ID (required). */
    private Long id;

    /** A list of email addresses of Guest users (required). */
    private List<String> guests;
}
