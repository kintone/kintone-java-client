package com.kintone.client.api.common;

import com.kintone.client.api.KintoneResponseBody;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** A response object for Bulk Requests API. */
@Getter
@RequiredArgsConstructor
public class BulkRequestsResponseBody implements KintoneResponseBody {

    /**
    * The response from each API request. The order of the response is the same as the order of the
    * requests.
    */
    private final List<KintoneResponseBody> results;
}
