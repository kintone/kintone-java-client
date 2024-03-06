# Kintone Java Client

API client library for Kintone REST APIs on Java.

## Installation

- For projects using Gradle  
    Add dependency declaration in `build.gradle` of your project.
    ```
    dependencies {
        implementation 'com.kintone:kintone-java-client:2.0.0'
    }
    ```
- For projects using Maven  
    Add dependency declaration in `pom.xml` of your project.
    ```
    <dependency>
        <groupId>com.kintone</groupId>
        <artifactId>kintone-java-client</artifactId>
        <version>2.0.0</version>
    </dependency>
  ```

## Compatibility

Java 8 and later version is supported.

## Basic Usage

Let show some short examples of this library.
For more information, read [documents](https://kintone.github.io/kintone-java-client/).

### Initialization

```
// Creates a client that uses Password authentication
KintoneClient client = KintoneClientBuilder.create("https://{your Kintone domain}.cybozu.com")
                                           .authByPassword(username, password)
                                           .build();
```

### Getting Records

```
// Gets a record
Record record = client.record().getRecord(1, 100);
String text = record.getSingleLineTextFieldValue("field1");

// Gets records that maches the query
List<Record> records = client.record().getRecords(1, "number > 100");
```

### Adding a Record

```
Record record = new Record();
record.putField("field1", new SingleLineTextFieldValue("Hello World");
long recordId = client.record().addRecord(1, record);
```

### App Settings

```
// Gets general information of an App
App app = client.app().getApp(1);

// Create a new App
long appId = client.app().addApp("New App");

// Add a field
Map<String, FieldProperty> properties = new HashMap<>();
properties.put("number", new NumberFieldProperty().setLabel("Number").setCode("number"));
client.app().addFormFields(appId, properties);

// Deploy an App
client.app().deployApp(appId);
AppDeployStatus status = client.app().getDeployStatus(appId);
```

### Cleanup

The client holdes some resources inside it.
Be sure to release them by calling `KintoneClient#close()`.

```
client.close();
```

## Building

This library is built using Gradle.

Run the following command to build a JAR file of this library.

```
$ ./gradlew clean jar
```

## Contribution Guide

- [CONTRIBUTING.md](CONTRIBUTING.md)

## License

- [MIT License](LICENSE)

## Author

Cybozu, Inc.

## Contributors

- AaronJRubin [@AaronJRubin](https://github.com/AaronJRubin)
