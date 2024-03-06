package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.Group;
import com.kintone.client.model.Organization;
import com.kintone.client.model.User;
import com.kintone.client.model.record.CalcFieldValue;
import com.kintone.client.model.record.CategoryFieldValue;
import com.kintone.client.model.record.CheckBoxFieldValue;
import com.kintone.client.model.record.CreatedTimeFieldValue;
import com.kintone.client.model.record.CreatorFieldValue;
import com.kintone.client.model.record.DateFieldValue;
import com.kintone.client.model.record.DateTimeFieldValue;
import com.kintone.client.model.record.DropDownFieldValue;
import com.kintone.client.model.record.FileFieldValue;
import com.kintone.client.model.record.GroupSelectFieldValue;
import com.kintone.client.model.record.LinkFieldValue;
import com.kintone.client.model.record.ModifierFieldValue;
import com.kintone.client.model.record.MultiLineTextFieldValue;
import com.kintone.client.model.record.MultiSelectFieldValue;
import com.kintone.client.model.record.NumberFieldValue;
import com.kintone.client.model.record.OrganizationSelectFieldValue;
import com.kintone.client.model.record.RadioButtonFieldValue;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.RecordNumberFieldValue;
import com.kintone.client.model.record.RichTextFieldValue;
import com.kintone.client.model.record.SingleLineTextFieldValue;
import com.kintone.client.model.record.StatusAssigneeFieldValue;
import com.kintone.client.model.record.StatusFieldValue;
import com.kintone.client.model.record.SubtableFieldValue;
import com.kintone.client.model.record.TableRow;
import com.kintone.client.model.record.TimeFieldValue;
import com.kintone.client.model.record.UpdatedTimeFieldValue;
import com.kintone.client.model.record.UserSelectFieldValue;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecordSerializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Record.class, new RecordSerializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void serialize_emptyRecord() throws IOException {
        String json = mapper.writeValueAsString(new Record());
        assertThat(json).isEqualTo("{}");
    }

    @Test
    public void serialize_CALC() throws IOException {
        Record record =
                new Record()
                        .putField("calc", new CalcFieldValue("100"))
                        .putField("calc_date", new CalcFieldValue("2022-01-01"))
                        .putField("calc_null", new CalcFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{}");
    }

    @Test
    public void serialize_CATEGORY() throws IOException {
        List<String> value = Collections.singletonList("category A");
        Record record = new Record().putField("category", new CategoryFieldValue(value));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{}");
    }

    @Test
    public void serialize_CHECK_BOX() throws IOException {
        Record record =
                new Record().putField("check_box", new CheckBoxFieldValue("option 2", "option 1"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"check_box\":{\"value\":[\"option 2\",\"option 1\"]}}");
    }

    @Test
    public void serialize_CHECK_BOX_empty() throws IOException {
        Record record = new Record().putField("check_box", new CheckBoxFieldValue());
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"check_box\":{\"value\":[]}}");
    }

    @Test
    public void serialize_CREATED_TIME() throws IOException {
        ZonedDateTime time = ZonedDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.UTC);
        Record record = new Record().putField("created_time", new CreatedTimeFieldValue(time));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"created_time\":{\"value\":\"2020-01-02T03:04Z\"}}");
    }

    @Test
    public void serialize_CREATED_TIME_negativeNumberYear() throws IOException {
        ZonedDateTime time = ZonedDateTime.of(-2020, 1, 2, 3, 4, 5, 6, ZoneOffset.UTC);
        Record record = new Record().putField("created_time", new CreatedTimeFieldValue(time));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"created_time\":{\"value\":\"-2020-01-02T03:04Z\"}}");
    }

    @Test
    public void serialize_CREATED_TIME_empty() throws IOException {
        Record record = new Record().putField("created_time", new CreatedTimeFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"created_time\":{\"value\":null}}");
    }

    @Test
    public void serialize_CREATOR() throws IOException {
        User user = new User("User", "user");
        Record record = new Record().putField("creator", new CreatorFieldValue(user));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"creator\":{\"value\":{\"code\":\"user\"}}}");
    }

    @Test
    public void serialize_CREATOR_empty() throws IOException {
        Record record = new Record().putField("creator", new CreatorFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"creator\":{\"value\":null}}");
    }

    @Test
    public void serialize_DATE() throws IOException {
        Record record = new Record().putField("date", new DateFieldValue(LocalDate.of(2020, 1, 2)));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"date\":{\"value\":\"2020-01-02\"}}");
    }

    @Test
    public void serialize_DATE_negativeNumberYear() throws IOException {
        Record record = new Record().putField("date", new DateFieldValue(LocalDate.of(-2020, 1, 2)));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"date\":{\"value\":\"-2020-01-02\"}}");
    }

    @Test
    public void serialize_DATE_empty() throws IOException {
        Record record = new Record().putField("date", new DateFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"date\":{\"value\":null}}");
    }

    @Test
    public void serialize_DATETIME() throws IOException {
        ZonedDateTime value = ZonedDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.UTC);
        Record record = new Record().putField("datetime", new DateTimeFieldValue(value));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"datetime\":{\"value\":\"2020-01-02T03:04Z\"}}");
    }

    @Test
    public void serialize_DATETIME_negativeNumberYear() throws IOException {
        ZonedDateTime value = ZonedDateTime.of(-2020, 1, 2, 3, 4, 5, 6, ZoneOffset.UTC);
        Record record = new Record().putField("datetime", new DateTimeFieldValue(value));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"datetime\":{\"value\":\"-2020-01-02T03:04Z\"}}");
    }

    @Test
    public void serialize_DATETIME_empty() throws IOException {
        Record record = new Record().putField("datetime", new DateTimeFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"datetime\":{\"value\":null}}");
    }

    @Test
    public void serialize_DROP_DOWN() throws IOException {
        Record record = new Record().putField("drop_down", new DropDownFieldValue("option 1"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"drop_down\":{\"value\":\"option 1\"}}");
    }

    @Test
    public void serialize_DROP_DOWN_empty() throws IOException {
        Record record = new Record().putField("drop_down", new DropDownFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"drop_down\":{\"value\":null}}");
    }

    @Test
    public void serialize_FILE() throws IOException {
        FileBody file1 =
                new FileBody().setName("a.txt").setContentType("text/plain").setSize(10).setFileKey("key1");
        FileBody file2 =
                new FileBody()
                        .setName("b.png")
                        .setContentType("image/png")
                        .setSize(1000)
                        .setFileKey("key2");
        Record record = new Record().putField("file", new FileFieldValue(file1, file2));
        String json = mapper.writeValueAsString(record);
        assertThat(json)
                .isEqualTo("{\"file\":{\"value\":[{\"fileKey\":\"key1\"},{\"fileKey\":\"key2\"}]}}");
    }

    @Test
    public void serialize_FILE_empty() throws IOException {
        Record record = new Record().putField("file", new FileFieldValue());
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"file\":{\"value\":[]}}");
    }

    @Test
    public void serialize_GROUP_SELECT() throws IOException {
        Group group1 = new Group("Group 1", "group1");
        Group group2 = new Group("Group 2", "group2");
        Record record =
                new Record().putField("group_select", new GroupSelectFieldValue(group1, group2));
        String json = mapper.writeValueAsString(record);
        assertThat(json)
                .isEqualTo("{\"group_select\":{\"value\":[{\"code\":\"group1\"},{\"code\":\"group2\"}]}}");
    }

    @Test
    public void serialize_GROUP_SELECT_empty() throws IOException {
        Record record = new Record().putField("group_select", new GroupSelectFieldValue());
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"group_select\":{\"value\":[]}}");
    }

    @Test
    public void serialize_LINK() throws IOException {
        Record record = new Record().putField("link", new LinkFieldValue("http://example.com"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"link\":{\"value\":\"http://example.com\"}}");
    }

    @Test
    public void serialize_LINK_empty() throws IOException {
        Record record = new Record().putField("link", new LinkFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"link\":{\"value\":null}}");
    }

    @Test
    public void serialize_MODIFIER() throws IOException {
        User user = new User("User", "user");
        Record record = new Record().putField("modifier", new ModifierFieldValue(user));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"modifier\":{\"value\":{\"code\":\"user\"}}}");
    }

    @Test
    public void serialize_MODIFIER_empty() throws IOException {
        Record record = new Record().putField("modifier", new ModifierFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"modifier\":{\"value\":null}}");
    }

    @Test
    public void serialize_MULTI_LINE_TEXT() throws IOException {
        Record record =
                new Record().putField("text_area", new MultiLineTextFieldValue("test 1\ntest 2"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"text_area\":{\"value\":\"test 1\\ntest 2\"}}");
    }

    @Test
    public void serialize_MULTI_LINE_TEXT_empty() throws IOException {
        Record record = new Record().putField("text_area", new MultiLineTextFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"text_area\":{\"value\":null}}");
    }

    @Test
    public void serialize_MULTI_SELECT() throws IOException {
        Record record =
                new Record().putField("multi_select", new MultiSelectFieldValue("option 3", "option 1"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"multi_select\":{\"value\":[\"option 3\",\"option 1\"]}}");
    }

    @Test
    public void serialize_MULTI_SELECT_empty() throws IOException {
        Record record = new Record().putField("multi_select", new MultiSelectFieldValue());
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"multi_select\":{\"value\":[]}}");
    }

    @Test
    public void serialize_NUMBER() throws IOException {
        Record record =
                new Record().putField("number", new NumberFieldValue(new BigDecimal("-0.0001")));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"number\":{\"value\":\"-0.0001\"}}");

        String bigNumber = "9999999999999999999999999999999999999999";
        record = new Record().putField("number", new NumberFieldValue(new BigDecimal(bigNumber)));
        json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"number\":{\"value\":\"" + bigNumber + "\"}}");
    }

    @Test
    public void serialize_NUMBER_empty() throws IOException {
        Record record = new Record().putField("number", new NumberFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"number\":{\"value\":null}}");
    }

    @Test
    public void serialize_ORGANIZATION_SELECT() throws IOException {
        Organization org1 = new Organization("Organization 1", "org1");
        Organization org2 = new Organization("Organization 2", "org2");
        Record record =
                new Record().putField("org_select", new OrganizationSelectFieldValue(org2, org1));
        String json = mapper.writeValueAsString(record);
        assertThat(json)
                .isEqualTo("{\"org_select\":{\"value\":[{\"code\":\"org2\"},{\"code\":\"org1\"}]}}");
    }

    @Test
    public void serialize_ORGANIZATION_SELECT_empty() throws IOException {
        Record record = new Record().putField("org_select", new OrganizationSelectFieldValue());
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"org_select\":{\"value\":[]}}");
    }

    @Test
    public void serialize_RADIO_BUTTON() throws IOException {
        Record record = new Record().putField("radio_button", new RadioButtonFieldValue("option 1"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"radio_button\":{\"value\":\"option 1\"}}");
    }

    @Test
    public void serialize_RADIO_BUTTON_empty() throws IOException {
        Record record = new Record().putField("radio_button", new RadioButtonFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"radio_button\":{\"value\":null}}");
    }

    @Test
    public void serialize_RECORD_NUMBER() throws IOException {
        Record record = new Record().putField("record_number", new RecordNumberFieldValue("100"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{}");
    }

    @Test
    public void serialize_RICH_TEXT() throws IOException {
        Record record = new Record().putField("rich_text", new RichTextFieldValue("<div>test</div>"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"rich_text\":{\"value\":\"<div>test</div>\"}}");
    }

    @Test
    public void serialize_RICH_TEXT_empty() throws IOException {
        Record record = new Record().putField("rich_text", new RichTextFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"rich_text\":{\"value\":null}}");
    }

    @Test
    public void serialize_SINGLE_LINE_TEXT() throws IOException {
        Record record = new Record().putField("text", new SingleLineTextFieldValue("test"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"text\":{\"value\":\"test\"}}");
    }

    @Test
    public void serialize_SINGLE_LINE_TEXT_empty() throws IOException {
        Record record = new Record().putField("text", new SingleLineTextFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"text\":{\"value\":null}}");
    }

    @Test
    public void serialize_STATUS() throws IOException {
        Record record = new Record().putField("status", new StatusFieldValue("state 1"));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{}");
    }

    @Test
    public void serialize_STATUS_ASSIGNEE() throws IOException {
        List<User> value = Collections.singletonList(new User("User", "user"));
        Record record = new Record().putField("assignee", new StatusAssigneeFieldValue(value));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{}");
    }

    @Test
    public void serialize_SUBTABLE() throws IOException {
        TableRow row1 = new TableRow().putField("text", new SingleLineTextFieldValue("test 1"));
        TableRow row2 = new TableRow().putField("text", new SingleLineTextFieldValue("test 2"));
        Record record = new Record().putField("table", new SubtableFieldValue(row1, row2));
        String json = mapper.writeValueAsString(record);
        assertThat(json)
                .isEqualTo(
                        "{\"table\":{\"value\":[{\"value\":{\"text\":{\"value\":\"test 1\"}}},{\"value\":{\"text\":{\"value\":\"test 2\"}}}]}}");
    }

    @Test
    public void serialize_SUBTABLE_empty() throws IOException {
        Record record = new Record().putField("table", new SubtableFieldValue());
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"table\":{\"value\":[]}}");
    }

    @Test
    public void serialize_TIME() throws IOException {
        Record record = new Record().putField("time", new TimeFieldValue(LocalTime.of(0, 0)));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"time\":{\"value\":\"00:00\"}}");
    }

    @Test
    public void serialize_TIME_empty() throws IOException {
        Record record = new Record().putField("time", new TimeFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"time\":{\"value\":null}}");
    }

    @Test
    public void serialize_UPDATED_TIME() throws IOException {
        ZonedDateTime time = ZonedDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.UTC);
        Record record = new Record().putField("updated_time", new UpdatedTimeFieldValue(time));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"updated_time\":{\"value\":\"2020-01-02T03:04Z\"}}");
    }

    @Test
    public void serialize_UPDATED_TIME_negativeNumberYear() throws IOException {
        ZonedDateTime time = ZonedDateTime.of(-2020, 1, 2, 3, 4, 5, 6, ZoneOffset.UTC);
        Record record = new Record().putField("updated_time", new UpdatedTimeFieldValue(time));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"updated_time\":{\"value\":\"-2020-01-02T03:04Z\"}}");
    }

    @Test
    public void serialize_UPDATED_TIME_empty() throws IOException {
        Record record = new Record().putField("updated_time", new UpdatedTimeFieldValue(null));
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"updated_time\":{\"value\":null}}");
    }

    @Test
    public void serialize_USER_SELECT() throws IOException {
        User user1 = new User("User 1", "user1");
        User user2 = new User("User 2", "user2");
        Record record = new Record().putField("user_select", new UserSelectFieldValue(user1, user2));
        String json = mapper.writeValueAsString(record);
        assertThat(json)
                .isEqualTo("{\"user_select\":{\"value\":[{\"code\":\"user1\"},{\"code\":\"user2\"}]}}");
    }

    @Test
    public void serialize_USER_SELECT_empty() throws IOException {
        Record record = new Record().putField("user_select", new UserSelectFieldValue());
        String json = mapper.writeValueAsString(record);
        assertThat(json).isEqualTo("{\"user_select\":{\"value\":[]}}");
    }
}
