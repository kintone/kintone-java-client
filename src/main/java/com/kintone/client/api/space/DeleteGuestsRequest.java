package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Delete Guests API. */
@Data
public class DeleteGuestsRequest implements KintoneRequest {

    /** A list of email addresses of Guest users (required). */
    private List<String> guests;
}
