package com.kintone.client.model;

import com.kintone.client.KintoneApi;
import com.kintone.client.api.KintoneRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** An object containing request information that run in a Bulk Request. */
@Getter
@RequiredArgsConstructor
public class BulkRequestContent {

    /** The path of the API for the request. */
    private final KintoneApi api;

    /**
     * The parameters to be passed onto the API of the request. Contents and formats will change
     * depending on the API.
     */
    private final KintoneRequest payload;
}
