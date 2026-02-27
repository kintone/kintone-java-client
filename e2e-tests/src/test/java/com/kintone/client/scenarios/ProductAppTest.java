package com.kintone.client.scenarios;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import org.junit.jupiter.api.Test;

public class ProductAppTest extends ApiTestBase {

    @Test
    public void run() {
        KintoneClient client = setupDefaultClient();
        String loginUser = getDefaultUser();

        ProductMaster master = new ProductMaster(client, loginUser);
        long masterAppId = master.run();

        ProductArrival arrival = new ProductArrival(client, masterAppId);
        long arrivalAppId = arrival.run();
    }
}
