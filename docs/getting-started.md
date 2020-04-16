## Getting Started

### Overview

This document presents the installation steps of Kintone Java Client and the basic usages of it.
This library allows you to work with your data on Kintone, like the following code.

```java
// Setup
KintoneClient client = KintoneClientBuilder.defaultClient("https://example.kintone.com", "user", "password");

// Retrieves a record (ID: 10) from App 1
Record record = client.record().getRecord(1, 10);

// Gets a number value from the record
BigDecimal value = record.getNumberFieldValue("field1");

// Adds a record to App 1
Record newRecord = new Record().putField("field1", new NumberFieldValue(value));
client.record().addRecord(1, newRecord);

// Cleanup
client.close();
```

### Installation

Download `kintone-java-client-0.9.0.jar` and import it into your Java project.

#### For projects using Gradle

1. Download the JAR file.
2. Add dependency declaration in `build.gradle` of your project.
    ```groovy
    dependencies {
         implementation files('{path to jar}/kintone-java-client-0.9.0.jar')
         implementation 'org.apache.httpcomponents:httpclient:4.5.12'
         implementation 'org.apache.httpcomponents:httpmime:4.5.12'
         implementation 'com.fasterxml.jackson.core:jackson-databind:2.10.3'
         implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.3'
    }
    ```

#### For projects using Maven
1. Download the JAR file.
2. Locally install the jar file by following command.
    ```shell
    $ mvn install:install-file -Dfile=kintone-java-client-0.9.0.jar -DgroupId=com.kintone -DartifactId=kintone-java-client -Dversion=0.9.0 -Dpackaging=jar
    ```
3. Add dependency declaration in `pom.xml` of your project.
    ```xml
    <dependency>
        <groupId>com.kintone</groupId>
        <artifactId>kintone-java-client</artifactId>
        <version>0.9.0</version>
    </dependency>
    <dependency>
      <groupId>org.apache.httpcomponents</groupId>
      <artifactId>httpclient</artifactId>
      <version>4.5.12</version>
    </dependency>
    <dependency>
      <groupId>org.apache.httpcomponents</groupId>
      <artifactId>httpmime</artifactId>
      <version>4.5.12</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.10.3</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.datatype</groupId>
      <artifactId>jackson-datatype-jsr310</artifactId>
      <version>2.10.3</version>
    </dependency>
    ```

### Setup Client

`KintoneClient` class is the core class of this library, which provides Java interface corresponding to Kintone REST APIs.

To setup the client, creates an instance of `KintoneClient` using `KintoneClientBuilder` class.
A simple setup using Password Authentication is as follows.

```java
// Creates a client that uses Password authentication.
String baseUrl = "https://{your Kintone domain}.cybozu.com";
KintoneClient client = KintoneClientBuilder.create(baseUrl)
                                           .authByPassword("user", "password")
                                           .build();
```

- Specify your Kintone Host URL (e.g. `https://example.cybozu.com` or `https://example.kintone.com`) to `create()` method
- For Password authentication, use `authByPassword()` method.
  This method requires the login name and the password of the user.

`KintoneClientBuilder` also provides some shorthand methods for typical setups.
The following code is the same as the example above.

```java
client = KintoneClientBuilder.defaultClient(baseUrl, "user", "password");
```

Then, you can call Kintone APIs via the client.
Before proceeding to each API usage, note the cleaning up of the client.
After using the client, be sure to call `KintoneClient#close()` to release resources held internally.

```java
client.close();
```

You can use try-with-resources statement to automatically and certainly call `close()`.

```java
try (KintoneClient client = KintoneClientBuilder.defaultClient(baseUrl, username, password)) {
    // Do some tasks using the client.
    ...
}
```

#### API Token Authentication

This library also supports API token authentication of Kintone.
`authByApiToken()` method initializes the client with API token(s).

```java
String token = "{your API token}";
client = KintoneClientBuilder.create(baseUrl)
                             .authByApiToken(token)
                             .build();
```

Note that API token authentication partially supports Kintone REST APIs.
This client does not check whether each API can be used with this authentication.
Please read Kintone REST API specifications to find supported operations with API tokens.

#### Additional Authentications

If your Kintone enables BASIC authentication or the client certificate authentication,
sets additional authentication properties.

```java
// with BASIC authentication
client = KintoneClientBuilder.create(baseUrl)
                             .authByPassword("user", "password")
                             .withBasicAuth("basic user", "basic password")
                             .build();

// with the client certificate authentication
// Specify your Kintone URL with ".s" to access Kintone using the client certificate.
String withClientCertURL = "https://{your Kintone domain}.s.cybozu.com";
Path path = Paths.get("{path to client certificate file}")
client = KintoneClientBuilder.create(withClientCertURL)
                             .authByPassword("user", "password")
                             .withClientCertificate(path, "certificate password")
                             .build();
```

#### Guest Space

To operate with a Guest Space and Apps in Guest Spaces, Kintone requests to embed the ID of operating Guest Space
into the path of request URL, like `/k/guest/123/v1/record.json`.

For such Guest Space accesses, `KintoneClient` uses preset Guest Space ID specified by `KintoneClientBuilder#setGuestSpaceId()`.
If you work with multiple Guest Spaces, you must setup the client for each Space individually.

```java
// A client for Guest Space (ID: 1)
KintoneClient clientA = KintoneClientBuilder.create(baseURL)
                                            .authByPassword("user", "password")
                                            .setGuestSpaceId(1)
                                            .build();
// Another client for Guest Space (ID: 2)
KintoneClient clientB = KintoneClientBuilder.create(baseURL)
                                            .authByPassword("user", "password")
                                            .setGuestSpaceId(2)
                                            .build();

// Copies a text value from an App in the Guest Space 1 to another App in the Guest Space 2
Record record = clientA.record().getRecord(app1, 1);
String value = record.getSingleLineTextFieldValue("text");

Record newRecord = new Record().putField("text", new SingleLineTextFieldValue(value));
clientB.record().addRecord(app2, newRecord);
```

### Record Operations

`KintoneClient` supports record related operations through the `RecordClient` subcomponent.
It can be obtained by `KintoneClient#record()`.
The methods of `RecordClient` are corresponding to Kintone APIs that operate on records.

#### Getting Records

To get a record (ID 100) from the App 1, use `RecordClient#getRecord()`.

```java
// Gets a record
Record record = client.record().getRecord(1, 100);
```

`RecordClient` provides some method for listing records, corresponding to `GET /k/v1/records.json` REST API.

```java
// Gets up to 20 records from APP 1 in descending order of Record numbers, with the offset is 300
List<Record> records = client.record().getRecords(1, 20, 300);

// Gets records from APP1 filtered by the specified condition
List<Record> records = client.record().getRecords(1, "number > 100");
```

Note that specifying huge offsets may cause a performance degradation while retrieving too many records.
If you plan to run the Get Records REST API multiple times with incrementally increasing the offsets to retrieve many records,
please consider using [the seek method](https://developer.kintone.io/hc/en-us/articles/360014037114) commonly known in RDBMSs
or using the Cursor API described below instead.

##### Using Cursor APIs

Kintone has Cursor APIs for retrieving thousands or more records sequentially.
`RecordClient` provides Cursor APIs as `createCursor()` and `getRecordsByCursor()`.

```java
// Creates a cursor for retrieving records
String cursorId = client.record().createCursor(1);
while (true) {
    // Fetches next records using the cursor
    GetRecordsByCursorResponseBody resp = client.record().getRecordsByCursor(cursorId);

    // Do something with fetched records
    List<Record> records = resp.getRecords();
    ...

     // Breaks the loop if no more records remaining
    if (!resp.hasNext()) {
        break;
    }
}
```

#### Creating Records

The next example shows how to add a record to an App.
It creates a record with "30" in a number filed and "Hello" in a text field.

```java
// Creates a record object
Record record = new Record();
// "number" and "text" are the field codes of the Number and Text field, respectively.
record.putField("number", new NumberFieldValue(30));
record.putField("text", new SingleLineTextFieldValue("Hello"));

// Adds the record to App 1
long recordId = client.record().addRecord(1, record);
```

##### Copying Records

To copy an existing record, you may use `Record#newFrom()` to create the `Record` object to be added.

`KintoneClient` sends the record data including the value of Creator field, Created date-time field,
Modifier field and Updated date-time field to Kintone if those fields exist in the posting record object.
Setting these field value requires App management permissions of the App, otherwise the request fails.

In most cases, you don't need to set those values explicitly, you will want to remove those fields from
the posting records so that those fields set automatically. `Record#newFrom()` provides the functionality
creates a new record object from the specified record without built-in fields including Creator field etc.

```java
// Gets a record
Record record = client.record().getRecord(1, 100);

// Creates new Record object from the retrieved record,
// copying without built-in fields such as Record Number and Created date and time.
Record copiedRecord = Record.newFrom(record);

// Adds the record
long recordId = client.record().addRecord(1, copiedRecord);
```

#### Updating Records

This example shows that the client updates the record ID of 100 of App 1 with
the "number" field with the value by adding 10.

```java
// Gets a record
Record record = client.record().getRecord(1, 100);
BigDecimal old = record.getNumberFieldValue("number");

// Creates a record for the update request
BigDecimal value = old.add(new BigDecimal(10));
Record updatedRecord = new Record();
updatedRecord.putField("number", new NumberFieldValue(value));

// Updates the record
long recordId = client.record().updateRecord(1, 100, updatedRecord);
```

You can also initialize a record for the update request from an existing record
using `Record#newFrom()` as described in Copying records section.

#### Bulk Requests

The Bulk Request API allows multiple API requests to run on multiple kintone apps
in one REST API call. The below sample adds a record to App 1
and updates the status of record 10 in App 2 within a bulk request.

```java
// Creates a request of Add Record API
Record record = new Record();
record.putField("number", new NumberFieldValue(10));
AddRecordRequest addRecordRequest = new AddRecordRequest()
                                          .setApp(1L)
                                          .setRecord(record);

// Creates a request of Update Record Status API
UpdateRecordStatusRequest updateRecordStatusRequest = new UpdateRecordStatusRequest()
                                                            .setApp(2L)
                                                            .setId(10L)
                                                            .setAssignee("user")
                                                            .setAction("Next Action");

BulkRequestsRequest req = new BulkRequestsRequest();
req.registerAddRecord(addRecordRequest);                   // Register the Add Record request
req.registerUpdateRecordStatus(updateRecordStatusRequest); // Register the Update Record Status request

// Executes bulk requests
BulkRequestsResponseBody resp = client.bulkRequests(req);

// Processes responses
// The responses are placed into the same index as the request.
AddRecordResponseBody body1 = (AddRecordResponseBody) resp.getResults().get(0);
System.out.println("Created record ID: " + body1.getId());

UpdateRecordStatusRequest body2 = (UpdateRecordStatusRequest) resp.getResults().get(1);
System.out.println("Update the status. Revision:" + body2.getRevision());
```

### File Operations

`FileClient`, that is obtained by `KintoneClient#file()`,
supports downloading and uploading files.

#### Download

This example downloads a file from Kintone and
saves its content as a file of "example.txt".

```java
String fileKey = "{the file key}";
try (InputStream in = client.file().downloadFile(fileKey)) {
    Files.copy(in, Paths.get("example.txt"));
}
```

The `fileKey` is the identifier of a file saved in Kintone,
which found in values of File field in a record, the icon of App settings and so on.

For example, the next example downloads all files attached to "file" field of the record 10.
```java
// Gets a record
Record record = client.record().getRecord(1, 10);
List<FileBody> files = record.getFileFieldValue("file");

// Downloads all files in "file" field
for (FileBody file : files) {
    try (InputStream in = client.file().downloadFile(file.getFileKey())) {
        // Note: FileBody#getName() returns the name responded from the server as is.
        // Verify that the name is valid so as not to cause security issues in production codes.
        Files.copy(in, Paths.get(file.getName()));
    }
}
```

#### Upload

`FileClient#upload()` allows you to upload a file to Kintone.

```java
Path file = Paths.get("example.jpg");
String fileKey = client.file().uploadFile(file, "image/jpg");
```

This method requires the content type of a file as well as the file itself.
If your system supports `Files.probeContentType()` of `java.nio.file.Files`,
it might be useful to guess the content type.

The responded `fileKey` is a temporal identifier of the uploaded file.
Be sure to attach the file to a record or a setting that requires file attachment.
After the file is uploaded onto Kintone, the file will be removed after 3 days
if the file is not attached to any record or settings.

### App Operations

Getting information of Apps and updating the App settings are provided by `AppClient`.
`KintoneClient#app()` returns the client.

#### Getting Apps

To get general information on an App such as the name and the description, you can use a `AppClient#getApp()` method.

```java
// Gets general information of an App 1
App app = client.app().getApp(1);
```

If you want to get information on multiple Apps at a time, `getAppsByIds()` and `getAppsByCodes()` will be useful.

```java
// Gets information of App 1 and App 2
List<App> apps = client.app().getAppsByIds(Arrays.asList(1L, 2L));
```

Note that `getAppsByIds()` and `getAppsByCodes()` can only work with Password authentication,
because those methods use `GET /k/v1/apps.json` API which only supports Password authentication.
`getApp()` uses `GET /k/v1/app.json` API which supports both Password authentication and
API token authentication.

#### Creating an App

This example shows how to build an App from scratch.
The code assumes that the client has been setup with Password authentication,
for API token authentication doesn't support `addApp()` that calls `POST /k/v1/preview/app.json`.

```java
// Creates a new App
long appId = client.app().addApp("New App");

// Adds a field
Map<String, FieldProperty> properties = new HashMap<>();
properties.put("number", new NumberFieldProperty().setLabel("Number").setCode("number"));
client.app().addFormFields(appId, properties);

// Starts a deployment of the App
client.app().deployApp(appId);

// Waits for the completion of deployment
while (true){
    DeployStatus  status = client.app().getDeployStatus(1);
    if (status != DeployStatus.PROCESSING) {
        System.out.println("Deploy: " + status);
        break;
    }
    Thread.sleep(10 * 000);
}
```

The deployment of Apps may take several minutes due to complex changes of App settings.
The example waits for the processing to complete by checking the status of App.
