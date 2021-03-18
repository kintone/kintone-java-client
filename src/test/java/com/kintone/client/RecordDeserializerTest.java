package com.kintone.client;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.Group;
import com.kintone.client.model.Organization;
import com.kintone.client.model.User;
import com.kintone.client.model.record.CalcFieldValue;
import com.kintone.client.model.record.CheckBoxFieldValue;
import com.kintone.client.model.record.DateFieldValue;
import com.kintone.client.model.record.DateTimeFieldValue;
import com.kintone.client.model.record.DropDownFieldValue;
import com.kintone.client.model.record.FileFieldValue;
import com.kintone.client.model.record.GroupSelectFieldValue;
import com.kintone.client.model.record.LinkFieldValue;
import com.kintone.client.model.record.MultiLineTextFieldValue;
import com.kintone.client.model.record.MultiSelectFieldValue;
import com.kintone.client.model.record.NumberFieldValue;
import com.kintone.client.model.record.OrganizationSelectFieldValue;
import com.kintone.client.model.record.RadioButtonFieldValue;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.RichTextFieldValue;
import com.kintone.client.model.record.SingleLineTextFieldValue;
import com.kintone.client.model.record.SubtableFieldValue;
import com.kintone.client.model.record.TableRow;
import com.kintone.client.model.record.TimeFieldValue;
import com.kintone.client.model.record.UserSelectFieldValue;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecordDeserializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Record.class, new RecordDeserializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void deserialize_emptyRecord() throws IOException {
        URL url = getClass().getResource("RecordDeserializerTest_deserialize_emptyRecord.json");
        Record record = mapper.readValue(url, TestObject.class).getRecord();

        assertThat(record.getFieldCodes(true)).isEmpty();
        assertThat(record.getId()).isNull();
        assertThat(record.getRevision()).isNull();
        assertThat(record.getStatusAssigneeFieldValue()).isNull();
        assertThat(record.getCategoryFieldValue()).isNull();
        assertThat(record.getCreatorFieldValue()).isNull();
        assertThat(record.getCreatedTimeFieldValue()).isNull();
        assertThat(record.getRecordNumberFieldValue()).isNull();
        assertThat(record.getStatusFieldValue()).isNull();
        assertThat(record.getModifierFieldValue()).isNull();
        assertThat(record.getUpdatedTimeFieldValue()).isNull();
    }

    @Test
    public void deserialize_builtins() throws IOException {
        URL url = getClass().getResource("RecordDeserializerTest_deserialize_builtins.json");
        Record record = mapper.readValue(url, TestObject.class).getRecord();

        assertThat(record.getFieldCodes(true)).hasSize(8);
        assertThat(record.getId()).isEqualTo(1L);
        assertThat(record.getRevision()).isEqualTo(2L);
        assertThat(record.getStatusAssigneeFieldValue()).isEmpty();
        assertThat(record.getCategoryFieldValue()).containsExactly("Category A", "Category B");
        assertThat(record.getCreatorFieldValue())
                .usingRecursiveComparison()
                .isEqualTo(new User("User", "user"));
        assertThat(record.getCreatedTimeFieldValue())
                .isEqualTo(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        assertThat(record.getRecordNumberFieldValue()).isEqualTo("APP-1");
        assertThat(record.getStatusFieldValue()).isEqualTo("state 1");
        assertThat(record.getModifierFieldValue())
                .usingRecursiveComparison()
                .isEqualTo(new User("User", "user"));
        assertThat(record.getUpdatedTimeFieldValue())
                .isEqualTo(ZonedDateTime.of(2020, 1, 2, 3, 4, 0, 0, ZoneOffset.UTC));
    }

    @Test
    public void deserialize_fields() throws IOException {
        URL url = getClass().getResource("RecordDeserializerTest_deserialize_fields.json");
        Record record = mapper.readValue(url, TestObject.class).getRecord();

        FileBody file =
                new FileBody()
                        .setContentType("image/jpeg")
                        .setName("test.jpg")
                        .setSize(1000)
                        .setFileKey("key");
        ZonedDateTime dateTime = ZonedDateTime.of(2020, 1, 2, 3, 4, 0, 0, ZoneOffset.UTC);

        assertThat(record.getFieldCodes(true)).hasSize(18);
        assertThat(record.getFieldValue("calc")).isEqualTo(new CalcFieldValue(new BigDecimal(100)));
        assertThat(record.getFieldValue("check_box"))
                .isEqualTo(new CheckBoxFieldValue("option 1", "option 2"));
        assertThat(record.getFieldValue("date"))
                .isEqualTo(new DateFieldValue(LocalDate.of(2020, 1, 1)));
        assertThat(record.getFieldValue("datetime")).isEqualTo(new DateTimeFieldValue(dateTime));
        assertThat(record.getFieldValue("drop_down")).isEqualTo(new DropDownFieldValue("option 2"));
        assertThat(record.getFieldValue("file"))
                .isEqualTo(new FileFieldValue(Collections.singletonList(file)));
        assertThat(record.getFieldValue("group_select"))
                .isEqualTo(new GroupSelectFieldValue(new Group("Group", "group")));
        assertThat(record.getFieldValue("link")).isEqualTo(new LinkFieldValue("http://example.com/"));
        assertThat(record.getFieldValue("lookup")).isEqualTo(new NumberFieldValue(new BigDecimal(1)));
        assertThat(record.getFieldValue("multi_select"))
                .isEqualTo(new MultiSelectFieldValue("option 1", "option 2"));
        assertThat(record.getFieldValue("number")).isEqualTo(new NumberFieldValue(new BigDecimal(10)));
        assertThat(record.getFieldValue("org_select"))
                .isEqualTo(
                        new OrganizationSelectFieldValue(new Organization("Organization", "organization")));
        assertThat(record.getFieldValue("radio_button"))
                .isEqualTo(new RadioButtonFieldValue("option 1"));
        assertThat(record.getFieldValue("rich_text"))
                .isEqualTo(new RichTextFieldValue("<div>test</div>"));
        assertThat(record.getFieldValue("text")).isEqualTo(new SingleLineTextFieldValue("test"));
        assertThat(record.getFieldValue("text_area")).isEqualTo(new MultiLineTextFieldValue("test"));
        assertThat(record.getFieldValue("time")).isEqualTo(new TimeFieldValue(LocalTime.of(23, 59)));
        assertThat(record.getFieldValue("user_select"))
                .isEqualTo(new UserSelectFieldValue(new User("User", "user")));
    }

    @Test
    public void deserialize_SUBTABLE() throws IOException {
        URL url = getClass().getResource("RecordDeserializerTest_deserialize_SUBTABLE.json");
        Record record = mapper.readValue(url, TestObject.class).getRecord();

        assertThat(record.getFieldCodes(true)).hasSize(1);
        assertThat(record.getFieldValue("table")).isInstanceOf(SubtableFieldValue.class);

        List<TableRow> rows = record.getSubtableFieldValue("table");
        assertThat(rows).hasSize(1);
        TableRow row = rows.get(0);
        assertThat(row.getFieldCodes()).hasSize(2);
        assertThat(row.getFieldValue("text")).isEqualTo(new SingleLineTextFieldValue("test"));
        assertThat(row.getFieldValue("number")).isEqualTo(new NumberFieldValue(new BigDecimal(100)));
    }

    @Test
    public void deserialize_SUBTABLE_empty() throws IOException {
        URL url = getClass().getResource("RecordDeserializerTest_deserialize_SUBTABLE_empty.json");
        Record record = mapper.readValue(url, TestObject.class).getRecord();

        assertThat(record.getFieldCodes(true)).hasSize(1);
        assertThat(record.getFieldValue("table")).isInstanceOf(SubtableFieldValue.class);
        assertThat(record.getSubtableFieldValue("table")).isEmpty();
    }

    public static class TestObject {
        private Record record;

        public Record getRecord() {
            return record;
        }

        public void setRecord(Record record) {
            this.record = record;
        }
    }
}
