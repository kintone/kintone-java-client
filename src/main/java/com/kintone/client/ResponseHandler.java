package com.kintone.client;

import com.kintone.client.api.KintoneResponse;

interface ResponseHandler {
    void handle(KintoneResponse<?> response);
}
