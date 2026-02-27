package com.kintone.client.scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.model.app.DeployStatus;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.SingleLineTextFieldValue;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class SimpleTest extends ApiTestBase {

    @Test
    public void test() throws InterruptedException {
        KintoneClient client = setupDefaultClient();

        long appId = client.app().addApp("SimpleTest");
        SingleLineTextFieldProperty textFieldProperty =
                new SingleLineTextFieldProperty().setCode("text").setLabel("文字列");
        client.app().addFormFields(appId, Collections.singletonList(textFieldProperty));
        client.app().deployApp(appId);

        while (client.app().getDeployStatus(appId) != DeployStatus.SUCCESS) {
            Thread.sleep(1000);
        }

        Record record = new Record().putField("text", new SingleLineTextFieldValue("Test!"));
        long recordId = client.record().addRecord(appId, record);

        String value = client.record().getRecord(appId, recordId).getSingleLineTextFieldValue("text");
        assertThat(value).isEqualTo("Test!");
    }
}
