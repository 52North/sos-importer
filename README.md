# 52°North SOS Importer
[:arrow_forward: How to Run](#how-to-run)&nbsp;&nbsp;&nbsp;[:nut_and_bolt: How to Build](#how-to-build)&nbsp;&nbsp;&nbsp;[:pencil: How to Develop](#developers)

**Master**: <a href='https://build.dev.52north.org/jenkins/view/Sensor%20Web/job/sos-importer/'><img src='https://build.dev.52north.org/jenkins/buildStatus/icon?job=sos-importer' /></a>&nbsp;<a href="https://travis-ci.org/52North/sos-importer"><img src="https://travis-ci.org/52North/sos-importer.svg?branch=master" /></a>&nbsp;&nbsp;&nbsp;**Develop**: <a href='https://build.dev.52north.org/jenkins/view/Sensor%20Web/job/sos-importer_develop/'><img src='https://build.dev.52north.org/jenkins/buildStatus/icon?job=sos-importer_develop' /></a>&nbsp;<a href="https://travis-ci.org/52North/sos-importer"><img src="https://travis-ci.org/52North/sos-importer.svg?branch=develop" /></a>&nbsp;&nbsp;<sup><a href="#branches"><b>*</b></a></sup>

## Description

**Upload data to standardized observation repositories**

*The SOS Importer is a tool for importing observations into standardized observation repositories. This enables interoperable spatial data and information exchange.*

The SOS Importer is a tool for importing observations from CSV files into a running SOS instance. Those CSV files can either be locally available or remotely (FTP support). The application makes use of the wizard design pattern which guides the user through different steps. These and their purposes are briefly characterized in the table below.

**Wizard Module**

The **Wizard Module** is the GUI wizard for creating the configurations (metadata about the CSV file).

**Feeder Module**

The **Feeder Module** uses the configurations created by the Wizard Module for importing the data into a running SOS instance.

Please check the [how to run](#how-to-run) section for instructions to start the two modules.

<a href="https://wiki.52north.org/pub/SensorWeb/SosImporter/52n-sos-importer_screenshot_arranged-with-logo.png"><img width="400" alt="52n-sos-importer_screenshot_arranged-with-logo.png" src="https://wiki.52north.org/pub/SensorWeb/SosImporter/52n-sos-importer_screenshot_arranged-with-logo.png"></a>

:information_source: *Click to view a larger version of the image.*


### Requirements

The SOS Importer requires [JAVA](http://java.com/) 1.7+ and a running SOS instance (OGC SOS v1.0 or v2.0) to work. The wizard module requires a GUI capable system.


### Terminology

Several sensor web specific terms are used within this topic:

   * Feature of Interest
   * Observed Property
   * Unit Of Measurement
   * Procedure or Sensor

If you are not familiar with them, please take a look at [this explaining OGC tutorial](http://www.ogcnetwork.net/sos_2_0/tutorial/om). It's short and easy to understand.


### Step Description

| *Step* | *Description* |
| --- | --- |
| 1 | Choose a CSV file from the file system to publish in a SOS instance. Alternatively you can also obtain a CSV file from a remote FTP server. |
| 2 | Provide a preview of the CSV file and select settings for parsing (e.g. which character is used for separating columns) |
| 3 | Display the CSV file in tabular format and assign metadata to each column (e.g. indicate that the second column consists of measured values). Offer customizable settings for parsing (e.g. for date/time patterns) |
| 4 | In case of more than one date/time, feature of interest, observed property, unit of measurement, sensor identifier or position has been identified in step 3, select the correct associations to the according measured value columns (e.g. state that date/time in column 1 belongs to the measured values in column 3 and date/time in column 2 belongs to the measured values in column 4). When there is exactly one appearance of a certain type, automatically assign this type to all measured values |
| 5 | Check available metadata for completeness and ask the user to add information in case something is missing (e.g. EPSG-code for positions) |
| 6 | When there is no metadata of a particular type present in the CSV file (e.g. sensor id), let the user provide this information (e.g. name and URI of this sensor) |
| 7 | Enter the URL of a Sensor Observation Service where measurements and sensor metadata in the CSV file shall be uploaded to |
| 8 | Summarize the results of the configuration process and provide means for importing the data into the specified SOS instance |


## Design

The SOS importer projects consists of three modules now:

   * wizard
   * feeder
   * configuration xml bindings

The first two are "applications" using the third to do their work: enabling the user to store metadata about his CSV file and import the contained data into a running SOS (OGC schema 1.0.0 or 2.0.0) instance for one time or repeatedly. In this process the wizard module is used to create the xml configuration documents. It depends on the `52n-sos-importer-bindings` module to write the configuration files (the XML schema: [stable](https://github.com/52North/sos-importer/blob/master/52n-sos-importer-bindings/src/main/xsd/datafile_configuration.xsd), [development](https://github.com/52North/sos-importer/blob/develop/52n-sos-importer-bindings/src/main/resources/import-configuration.xsd)). The feeder module reads this configuration file and the defined data file, creates the requests for inserting the data, and registers the defined sensors in the SOS if not done already. For communicating with the SOS some modules of the OxFramework are used:

   * oxf-sos-adapter
   * oxf-common
   * oxf-feature
   * oxf-adapter-api
   * 52n-oxf-xmlbeans

Thanks to the new structure of the OxFramework the number of modules and dependencies is reduced. The following figure shows this structure.

<img width="381" alt="sos-importer-structure.png" src="https://wiki.52north.org/pub/SensorWeb/SosImporter/sos-importer-structure.png" height="150" />


### Wizard Module

The *wizard module* contains the following major packages:
   * `org.n52.sos.importer`
      * `controller` - contains all the business logic including a `StepController` for each step in the workflow and the `MainController` which controls the flow of the application.
      * `model` - contains all data holding classes, each for one step and the overall !XMLModel build using the `52n-sos-importer-bindings` module.
      * `view` - contains all views (here: `StepXPanel`) including the `MainFrame` and the `BackNextPanel`. The sub packages contain special panels required for missing resources or re-used tabular views.
A good starting point for new developers is to take a look at the `MainController.setStepController(!StepController)`, `BackNextController.nextButtonClicked()`, and the `StepController`. During each step the !XMLModel is updated `MainController.updateModel()`.

All important constants are stored in `org.n52.sos.importer.Constants`.


### Feeder Module

The *feeder module* contains the following major packages:
   * `org.n52.sos.importer.feeder` - contains classes `Configuration` and `DataFile` which offer means accessing the xml configuration and the csv data file, the application's main class `Feeder` which controls the application workflow, and the `SensorObservationService` class, which imports the data using the `DataFile` and `Configuration` classes for creating the required requests.
      * `model` - contains data holding classes for the resources like feature of interest, sensor, requests (insert observation, register/insert sensor)...
      * `task` - contains controllers required for one time and repeated feeding used by the central `Feeder` class.
A good starting point for new developers is to take a look at the `Feeder.main(String[])` and follow the path through the code. When changing something regarding communication with the SOS and data parsing, take a look at: `SensorObservationService`, `Configuration`, and `DataFile`.


## Configuration Schema

The configuration schema is used to store metadata about the data file and the import procedure. The following diagrams show the XML schema used within the wizard and feeder module for storing and re-using the metadata that is required to perform the import process. In other words, the configuration XML files use this schema. Theses configuration files are required to create messages which the SOS understands.


### SosImportConfiguration

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/01_SosImportConfiguration.png" alt="01_SosImportConfiguration.png" width='389' height='130' />

Each `SosImportConfiguration` contains three mandatory sections. These are `DataFile`, `SosMetadta`, and `CsvMetadata`. The section `AdditionalMetadata` is optional.

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/02_DataFile.png" alt="02_DataFile.png" width='663' height='715' />

The `DataFile` contains information about the file containing the observations. The attributes are described in the table below. The second section of the `DataFile` is the choice between `LocalFile` or `RemoteFile`. A `LocalFile` has a `Path` and two optional parameters. The `Encoding` is used while reading the file (e.g. Java would use the system default but the file is in other encoding. Example: UTF-8). The `RegularExpresssionForAllowedFileNames` is used to find files if `Path` is not a file but a directory (e.g. you might have a data folder with files from different sensors). A `RemoteFile` needs an `URL` and optional `Credentials` (:information_source: These are stored in plain text!). The last optional section of the `DataFile` is the `IgnoreLineRegEx` array. Here, you can define regular expressions to ignore lines which make problems during the import process or contain data that should not be imported.

| *Attribute* | *Description* | *Optional* |
| --- | --- | --- |
| `referenceIsARegularExpression` | If set to TRUE the LocaleFile.Path or RemoteFile.URL contains a regular expression needing special handling before retrieving the data file. |:x: |
| `useDateFromLastModifiedDate` | If set to TRUE the last modified date of the datafile will be used as date value for the observation of this data file.| :white_check_mark: |
| `lastModifiedDelta` | If available and `useDateFromLastModifiedDate` is set to TRUE, the date value will be set to `n` days before last modified date. | :white_check_mark: |
| `regExDateInfoInFileName` | If present, the contained regular expression will be applied to the file name of the datafile to extract date information in combination with the "dateInfoPattern". Hence, this pattern is used to extract the date string and the dateInfoPattern is used to convert this date string into valid date information. The pattern MUST contain one group that holds all date information! | :white_check_mark: |
| `dateInfoPattern` | MUST be set, if `regExDateInfoInFileName` is set, for converting the date string into valid date information. Supported pattern elements: `y`, `M`, `d`, `H`, `m`, `s`, `z`, `Z` | :white_check_mark: |
| `headerLine` | Identifies the header line. MUST be set in the case of having the header line repeatedly in the data file. | :white_check_mark: |
| `sampleStartRegEx` | Identifies the beginning of an new sample in the data file. Requires the presence of the following attributes: `sampleDateOffset`, `sampleDateExtractionRegEx`, `sampleDatePattern`, `sampleDataOffset`. A "sample" is a single samplingrun having additional metadata like date information which is not contained in the lines. | :white_check_mark: |
| `sampleDateOffset` | Defines the offset in lines between the first line of a sample and the line containing the date information. | :white_check_mark: |
| `sampleDateExtractionRegEx` | Regular expression to extract the date information from the line containing the date information of the current sample. The expression MUST result in ONE group. This group will be parsed to a `java.util.Date` using `sampleDatePattern` attribute. | :white_check_mark: |
| `sampleDatePattern` | Defines the pattern used to parse the date information of the current sample. | :white_check_mark: |
| `sampleDataOffset` | Defines the offset in lines from sample beginning till the first lines with data. | :white_check_mark: |
| `sampleSizeOffset` | Defines the offset in lines from sample beginning till the line containing the sample size in lines with data. | :white_check_mark: |
| `sampleSizeRegEx` | Defines a regular expression to extract the sample size. The regular expression MUST result in ONE group which contains an integer value. | :white_check_mark: |
| `sampleSizeDivisor` | Defines a divisor that is applied to the sample size. Can be used in cases the sample size is not giving the number of samples but the time span of the sample. The divisor is used to calculate the number of lines in a sample. | :white_check_mark: |


### SosMetadata

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/03_SosMetadata.png" alt="03_SosMetadata.png" width='383' height='238' />

The `SosMetadata` section has one optional attribute `insertSweArrayObservationTimeoutBuffer`, three mandatory sections `URL`, `Offering` with attribute `generate`, and `Version`. The section `Binding` is optional. The `insertSweArrayObservationTimeoutBuffer` is required if the import strategy `SweArrayObservationWithSplitExtension` (more details later) is used. It defines an additional timeout that's used when sending the !InsertObservation requests to the SOS. The `URL` defines the service endpoint that receives the requests (e.g. !Insert!|RegisterSensor, !InsertObservation). The `Offering" should contain the offering identifier to use, or its attribute `generate` should be set to true. Than, the sensor identifier is used as offering identifier. The `Version` section defines the OGC specification version, that is understood by the SOS instance, e.g. 1.0.0, 2.0.0. The optional `Binding` section is required when selecting SOS version 2.0.0 and defines which binding should be used, e.g. SOAP, POX.


### CsvMetadata

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/04_CsvMetadata.png" alt="04_CsvMetadata.png" width='482' height='278' />

The `CsvMetadata` contains information for the CSV parsing. The mandatory sections `DecimalSeparator`, `Parameter/CommentIndicator`, `Parameter/ColumnSeparator` and `Parameter/TextIndicator` define, how to parse the raw data into columns and rows. The optional `CsvParserClass` is required if another `CsvParser` implementation than the default is used (see [Extend CsvParser](#Extend_CsvParser) section below for more details). The `FirstLineWithData` defines how many lines should be skipped before the data content starts. The most complex and important section is the `ColumnAssignments` sections with contains 1..&infin; `Column` sections.

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/05_Column.png" alt="05_Column.png" width='532' height='724' />

The `Column` contains two mandatory sections `Number` and `Type`. The `Number` indicates to which column in the data file this metadata is related to. Counting starts with 0. The `Type` indicates the column type. The following types are supported:

**Type**

| *Type* | *Content of the column* |
| --- | --- |
| `DO_NOT_EXPORT` | Do not export this column. It should be ignored by the application. It's the default type. |
| `MEASURED_VALUE` | The result of the performed observation, in most cases some value. |
| `DATE_TIME` | The date or time or date and time of the performed observation. |
| `POSITION` | The position of the performed observation. |
| `FOI` | The feature of interest. |
| `OBSERVED_PROPERTY` | The observed property. |
| `UOM` | The Unit of measure using UCUM codes. |
| `SENSOR` | The sensor id. |

Some of these types require several `Metadata` elements, consisting of a `Key` and a `Value`. Currently supported values of `Key`:

**Key**

| *Key* | *Value* |
| --- | --- |
| `GROUP` | Indicates the membership of this column in a `POSITION` or `DATE_TIME` group. |
| `TIME` | Not used. |
| `TIME_DAY` | The day value for the time stamp for all observations in the related `MEASURED_VALUE` column. |
| `TIME_HOUR` | The hour value for the time stamp for all observations in the related `MEASURED_VALUE` column. |
| `TIME_MINUTE` | The minute value for the time stamp for all observations in the related `MEASURED_VALUE` column. |
| `TIME_MONTH` | The month value for the time stamp for all observations in the related `MEASURED_VALUE` column. |
| `TIME_SECOND` | The second value for the time stamp for all observations in the related `MEASURED_VALUE` column. |
| `TIME_YEAR` | The year value for the time stamp for all observations in the related `MEASURED_VALUE` column. |
| `TIME_ZONE` | The time zone value for the time stamp for all observations in the related `MEASURED_VALUE` column. |
| `TYPE` | If `MEASURED_VALUE` column than these values are supported: `NUMERIC`, `COUNT`, `BOOLEAN`, `TEXT`. If `DATE_TIME` column than these `COMBINATION`, `UNIX_TIME`. |
| `OTHER` | Not used. |
| `PARSE_PATTERN` | Used to store the parse pattern of a `POSITION` or `DATE_TIME` column. |
| `POSITION_ALTITUDE` | The altitude value for the positions for all observations in the related `MEASURED_VALUE` column. |
| `POSITION_EPSG_CODE` | The EPSG code for the positions for all observations in the related `MEASURED_VALUE` column. |
| `POSITION_LATITUDE` | The latitude value for the positions for all observations in the related `MEASURED_VALUE` column. |
| `POSITION_LONGITUDE` | The longitude value for the positions for all observations in the related `MEASURED_VALUE` column. |

The `RelatedDateTimeGroup` is required by an `MEASURED_VALUE` column and identifies all columns that contain information about the time stamp for an observation. The `RelatedMeasuredValueColumn` identifies the `MEASURED_VALUE` column for columns of other types, e.g. `DATE_TIME`, `SENSOR`, `FOI`. The `Related(FOI|ObservedProperty|Sensor|UnitOfMeasurement)` sections contain either a `IdRef` or a `Number`. The number denotes the `Column` that contains the value. The `IdRef` links to a `Resource` in the `AdditionalMetadata` section (:information_source: The value of `IdRef` is unique within the document and only for document internal links).


### AdditionalMetadata

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/06_AdditionalMetadata.png" alt="06_AdditionalMetadata.png" width='575' height='644' />

The `AdditionalMetadata` is the last of the four top level sections and it is optional. The intention is to provide additional metadata. These are generic `Metadata` elements, `Resources` like `Sensor`, `ObservedProperty`, `FeatureOfInterest`, `UnitOfMeasurement` and <code>FOIPosition</code>s. The table below lists the supported values for the `Metadata` elements.

| *Key* | *Value* |
| --- | --- |
| `IMPORT_STRATEGY` | The import strategy to use: `SingleObservation` (default strategy) or `SweArrayObservationWithSplitExtension`. The second one is only working if the SOS instance supports the [SplitDataArrayIntoObservations](https://wiki.52north.org/SensorWeb/SensorObservationServiceIVDocumentation#SplitDataArrayIntoObservations) request extension. It results in better performance and less data transfered. |
| `HUNK_SIZE` | Integer value defining the number of rows that should be combined in one SWEArrayObservation. |
| `OTHER` | Not used. Maybe used by other `CsvParser` implementations. |

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/07_Resource.png" alt="07_Resource.png" width='241' height='220' />

A `Resource` is a sensor, observed property, feature of interest or unit of measurement and it has a unique `ID` within each configuration. A resource can have a `Position` (e.g. a feature of interest). The information can be entered manually or it can be generated from values in the same line of the data file (`GeneratedResource`).

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/08_GeneratedResource.png" alt="08_GeneratedResource.png" width='496' height='503' />

The `Number` define the `Column` which content is used for the identifier and name generation. The order of the <code>Number</code>s is important. The optional `ConcatString` is used to combine the values from the different columns. The `URI` defines a URI for all <code>Resource</code>s, or it is used as prefixed for an generated `URI`, if `useAsPrefix` is set to `true`.

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/09_ManualResource.png" alt="09_ManualResource.png" width='438' height='399' />

A `ManualResource` has a `Name`, `URI` (when `useAsPrefix` is set, the `URI := URI + Name`).


## Road map

*Legend*:
   * :white_large_square: &rarr; denotes *future* versions and *not* implemented features
   * :white_check_mark: &rarr; denotes *achieved* versions and *implemented* features
   * [ ] &rarr; denotes open issues
   * [x] &rarr; denotes closed issues

:information_source: Dear developer, please update our [trello board](https://trello.com/b/kydEVMz3/sos-importer) accordingly!

### :white_large_square: Open Features

:information_source: Please add feature requests as [new issue](https://github.com/52North/sos-importer/issues/new) with label **enhancement**.

   * [ ] Allow regular expressions to describe dynamic directory/file names (repeated feeding)
   * [ ] Generic web client for multiple protocol support
   * [ ] Pushing new data directly into a SOS database through a database connection (via according SQL statements)
   * [ ] Feed to multiple SOS instances
   * [ ] Support SOAP binding (might be an OX-F task)
   * [ ] Support KVP binding (might be an OX-F task)



### :white_large_square: 0.5

   * [ ] Switch to `joda-time` or [EOL](https://docs.oracle.com/javase/8/docs/api/index.html?java/time/package-summary.html][java 8 DateTime API]] &rArr; switch to java 8 because of [[http://www.oracle.com/technetwork/java/eol-135779.html)
   * [ ] handle failing insertobservations, e.g. store in common csv format and re-import during next run.
   * [ ] Switch wizard to Java FX.
   * [x] [Fixed issues](https://github.com/52North/sos-importer/issues?utf8=%E2%9C%93&q=is%3Aclosed+milestone%3A0.5.0+)


### :white_check_mark: 0.4

   * *Code:* [github](https://github.com/52North/sos-importer/tree/52n-sos-importer-0.4.0)
   * *Features*
      * Rename *Core* module to *Wizard*
      * Support for SOS 2.0 incl. Binding definition
      * Start Screen offers button to see all dependency licenses
      * Support for sensors with multiple outputs
      * Introduced import strategies:
         * SingleObservation: `Default strategy`
         * SweArrayObservationWithSplitExtension: <br /> Reads `hunksize` # lines and imports each time series using an SWEArrayObservation in combination with the SplitExtension of the 52North SOS implementation. Hence, this strategy works only in combination with 52North implementation. Other impl. might work, too, but not as expected. Hunksize and import strategy are both optional `<AdditionalMetadata><Metadata>` elements.
      * Support for date information extraction from file name using two new OPTIONAL attributes in element `<DataFile>`:
         * "regExDateInfoInFileName" for extracting date information from file names.
         * "dateInfoPattern" for parsing the date information into a `java.util.Date`.
      * Date information extraction from last modified date using two new OPTIONAL attributes:
         * "useDateFromLastModifiedDate" for enabling this feature
         * "lastModifiedDelta" for moving the date n days back (this attribute is OPTIONAL for this feature, too.)
      * Ignore lines with regular expressions feature: 0..infinity elements can be added to the element. Each element will be used as regular expression and applied to each line of the data file before parsing.
      * Handling of data files containing several sample runs. A sample run contains additional metadata like its size (number of performed measurements) and a date. The required attributes are:
         * "sampleStartRegEx" - the start of a new sample (MUST match the whole line).
         * "sampleDateOffset" - the offset of the line containing the date of the sample from the start line.
         * "sampleDateExtractionRegEx" - the regular expression to extract the date information from the line containing the date information of the current sample. The expression MUST result in ONE group. This group will be parsed to a `java.util.Date` using "sampleDatePattern" attribute.
         * "sampleDatePattern" - the pattern used to parse the date information of the current pattern.
         * "sampleDataOffset" - the offset in lines from sample beginning till the first lines with data.
         * "sampleSizeOffset" - the offset in lines from sample beginning till the line containing the sample size in lines with data.
         * "sampleSizeRegEx" - the regular expression to extract the sample size. The regular expression MUST result in ONE group which contains an integer value.
      * Setting of timeout buffer for the insertion of SweArrayObservations: <br /> With the attribute "insertSweArrayObservationTimeoutBuffer" of `<SosMetadata>` it is possible to define an additional timeout buffer for connect and socket timeout when using import strategy "SweArrayObservationWithSplitExtension". Scale is in milliseconds, e.g. `1000` &rarr; 1s more connect and socket timeout. The size of this value is related to the set-up of the SOS server, importer, and the `HUNK_SIZE` value.<br/>The current OX-F SimpleHttpClient implementation uses a default value of 5s, hence setting this to `25,000` results in 30s connection and socket timeout.
      * More details can be found in the release notes.
   * *Fixed Bugs/Issues*
      * [#06](https://github.com/52North/sos-importer/issues/6): Hardcoded time zone in test
      * [#10](https://github.com/52North/sos-importer/issues/10): NPE during feeding if binding value is not set
      * [#11](https://github.com/52North/sos-importer/issues/11): !BadLocationException in the case of having empty lines in csv file
      * [#20](https://github.com/52North/sos-importer/issues/20): Current GUI is broken when using sample based files with minor inconsistencies
      * [#24](https://github.com/52North/sos-importer/issues/24): Fix/ignore line and column: Solved two NPEs while ignoring lines or columns
      * [#25](https://github.com/52North/sos-importer/issues/25): Fix/timezone-bug-parse-timestamps: Solved bug while parsing time stamps
      * #NN: Fix bug with timestamps of sample files
      * #NN: Fix bug with incrementing lastline causing data loss
      * #NN: Fix bug with data files without headerline
      * #NN: NSAMParser: Fix bug with timestamp extraction
      * #NN: NSAMParser: Fix bug with skipLimit
      * #NN: NSAMParser: Fix bug with empty lines, line ending, time series encoding
      * #NN: fix/combinationpanel: On step 3 it was not possible to enter parse patterns for position and date & time
      * #NN: fix problem with textfield for CSV file when switching to German      
      * #NN: fix problem with multiple sensors in CSV file and register sensor
      * [878](https://bugzilla.52north.org/show_bug.cgi?id=878)
      * "Too many columns issue"
      * [Fixed issues](https://github.com/52North/sos-importer/issues?utf8=%E2%9C%93&q=milestone%3A0.4.0+is%3Aclosed)
   * *Release files*:
      * *Feeder Module*: [Snapshots](https://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-feeder/0.4.0/?C=M;O=D) - :information_source: Download newest file ending with `-bin.jar`.
      * *Wizard Module*: [Snapshots](https://52north.org/maven/repo/snapshots/org/n52/sensorweb/52n-sos-importer-wizard/0.4.0/?C=M;O=D) - :information_source: Download newest file ending with `-bin.jar`.


### :white_check_mark: 0.3

   * *Release file:* [Core Module](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-core/0.3.0/52n-sos-importer-core-0.3.0-bin.jar) [md5](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-core/0.3.0/52n-sos-importer-core-0.3.0-bin.jar.md5), [Feeding Module](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-feeder/0.3.0/52n-sos-importer-feeder-0.3.0-bin.jar) [md5](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-feeder/0.3.0/52n-sos-importer-feeder-0.3.0-bin.jar.md5)
   * *Code:* [Github](https://github.com/52North/sos-importer/tree/0.3.0)
   * *Features*
      * [x] Use SOSWrapper from OXF
      * [x] Support more observation types
      * [x] FTP Remote File Support
   * *Fixed Bugs*
      * [736](https://bugzilla.52north.org/show_bug.cgi?id=736), [738](https://bugzilla.52north.org/show_bug.cgi?id=738), [650](https://bugzilla.52north.org/show_bug.cgi?id=650), [630](https://bugzilla.52north.org/show_bug.cgi?id=630), [645](https://bugzilla.52north.org/show_bug.cgi?id=645), [733](https://bugzilla.52north.org/show_bug.cgi?id=733), [629](https://bugzilla.52north.org/show_bug.cgi?id=629), [668](https://bugzilla.52north.org/show_bug.cgi?id=668), [589](https://bugzilla.52north.org/show_bug.cgi?id=589), [649](https://bugzilla.52north.org/show_bug.cgi?id=649), [669](https://bugzilla.52north.org/show_bug.cgi?id=669), [586](https://bugzilla.52north.org/show_bug.cgi?id=586), [707](https://bugzilla.52north.org/show_bug.cgi?id=707), [523](https://bugzilla.52north.org/show_bug.cgi?id=523)


### :white_check_mark: 0.2

   * *Release file:* [Core Module](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-core/0.2.0/52n-sos-importer-core-0.2.0-bin.jar) [md5](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-core/0.2.0/52n-sos-importer-core-0.2.0-bin.jar.md5), [Feeding Module](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-feeder/0.2.0/52n-sos-importer-feeder-0.2.0-bin.jar) [md5](http://52north.org/maven/repo/releases/org/n52/sensorweb/52n-sos-importer-feeder/0.2.0/52n-sos-importer-feeder-0.2.0-bin.jar.md5)
   * *Code:* [Github](https://github.com/52North/sos-importer/tree/0.2.0)
   * *Features*
      * [x] maven build
      * [x] multi language support
      * [x] xml configuration
      * [x] generation of FOIs and other data from columns
      * [x] feeding component


### :white_check_mark: 0.1

   * *Release file:* <a target="_blank" href="http://svn.52north.org/cgi-bin/viewvc.cgi/incubation/SOS-importer/tags/SOS-Importer-v0.1.zip?view=co&root=sensorweb" title="52n-sensorweb-sos-importer-0.1">52n-sensorweb-sos-importer-0.1</a>
   * *Code:* [Github](https://github.com/52North/sos-importer/tree/0.1.0)
   * *Features*
      * [x] Swing GUI
      * [x] CSV file support
      * [x] one time import


## Contributors

   * Active
      * [Eike J&uuml;rrens](https://github.com/EHJ-52n)
      * Your name here!
   * Former
      * Eric Fiedler
      * Raimund Schn&#252;rer
      * [Jan Schulte](https://github.com/janschulte)


## Get Involved

You may first get in touch using the [sensor web mailinglist](mailto:swe@52north.org) ([Mailman page including archive access](http://list.52north.org/mailman/listinfo/swe), [Forum view](http://sensorweb.forum.52north.org/) for browser addicted). In addition, you might follow the overall instruction about [getting involved with 52&#176;North](http://52north.org/about/get-involved/) which offers more than contributing as developer like designer, translator, writer, .... Your help is always welcome!


## Project Funding

   * by the European FP7 research project <a href="http://www.eo2heaven.org/" target="_blank">EO2HEAVEN</a> (co-funded by the European Commission under the grant agreement n&deg;244100).
   * by the European FP7 research project <a href="http://52north.org/resources/references/geostatistics/109-geoviqua" target="_blank">GeoViQua</a> (co-funded by the European Commission under the grant agreement n&deg;265178).
   * by University of Leicester during 2014.


# Users
## How to Run
### Module _feeder_

   1. Have *datafile* and *configuration file* ready.
   1. Open command line tool.
   1. Change to directory with `52n-sos-importer-feeder-$VERSION_NUMBER$-bin.jar`.
   1. Run `java -jar 52n-sos-importer-feeder-$VERSION_NUMBER$-bin.jar` to see the latest supported and required parameters like this:<br />
```
usage: java -jar Feeder.jar -c file [-d datafile] [-p period]
        options and arguments:
        -c file	 : read the config file and start the import process
        -d datafile : OPTIONAL override of the datafile defined in config file
        -p period   : OPTIONAL time period in minutes for repeated feeding
```


   * *Notes*
      * *Repeated Feeding*
         * Element `SosImportConfiguration::DataFile::LocalFile::Path`
            * ...set to a directory &rarr; the repeated feeding implementation will always take the newest file (regarding `java.io.File.lastModified()`) and skip the current run if no new file is available.
            * ...set to a file &rarr; the repeated feeding implementation will always try to import all found observations from the datafile (:information_source: the 52&#176;North SOS implementation prohibits inserting duplicate observations! &rarr; no problem when finding some in the data file!)


### Module _wizard_

   1. Open command line tool.
   1. Change to directory with `52n-sos-importer-wizard-$VERSION_NUMBER$-bin.jar`.
   1. Run `java -jar 52n-sos-importer-wizard-$VERSION_NUMBER$-bin.jar`.
   1. Follow the wizard to create a configuration file which can be used by the feeder module for repeated feeding or import the data once using the wizard (the second option requires the latest `52n-sos-importer-feeder-$VERSION_NUMBER$-bin.jar` in the same folder like the `52n-sos-importer-wizard-$VERSION_NUMBER$-bin.jar`.


## Quickstart

Follow this list of steps or [this user guide](http://52north.org/files/sensorweb/eo2h/52n-sensorweb-sos-importer-guide.pdf) using the [demo data](SosImporter#Demo_data) to get a first user experience.

   1. Download the example data to your computer.
   1. Start the application with `javar -jar 52n-sos-importer-wizard-bin.jar`.
   1. Select the file you have just downloaded on step 1 and click *next*.
   1. Increase the value of *Ignore data until line* to `1` and click *next*.
   1. Select *Date & Time* and than *Combination* and than provide the following date parsing pattern: `dd.MM.yyyy HH:mm` and click *next*.
   1. 3x Select *Measured Value* and than *Numeric Value* and click *next*.
   1. Set UTC offset to 0.
      <br />:information_source: If you want to import data reguarly, it is common sense to use UTC for timestamps.
   1. Feature of Interest: On the next view *Add missing metadata* select *Set Identifier manually*, click on the *pen* next to the *Name* label and enter any *name* and *URI* combination you can think of. Repeat this step 3 times (one for each time series). For time series #2 and #3 you can although select the previously entered value.
   1. Observed property: repeat as before but enter *name* and *URI* of the observed property for each timeseries, e.g. Propan, Water and Krypton.
   1. Unit of Measurement: repeat as before but enter *name* and *URI* of the unit of measurement for each timeseries, e.g. l,l,kg.
   1. Sensor: repeat as before but enter *name* and *URI* of the sensor for each timeseries, that performed the observations, e.g. propane-sensor, water-meter, crypto-graph.
   1. Define the position of the feature of interest manually, giving its coordinates.
   1. Next, specify the URL of the SOS instance, you want to import data into.
   1. Choose a folder to store the import configuration (for later re-use with the feeder module, for example).
   1. Specify the OGC specification version the SOS instance supports (We recommend to use `2.0.0`!).
   1. When using the 52N SOS, you can specify to use the import strategy SweArrayObservation which improves the performance of the communication between feeder and SOS a lot.
   1. On the last step, you can view the log file, configuration file or start the import procedure. That's it, now you should be able to import data into a running SOS instance from CSV files, or similar.


### Demo data

You can just download example files to see how the application works:
   * [example-data.csv](https://wiki.52north.org/pub/SensorWeb/SosImporter/example-data.csv): Example CSV file with 3 time series


# Developers
## Todos

:information_source: [Github issues are used to organize the work](https://github.com/52North/sos-importer/issues?utf8=%E2%9C%93&q=is%3Aopen+).


## How to Build

   1. Have jdk (>=1.7), maven (>=3.1.1), and git installed already.
   1. Due to some updates to the OX-Framework done during the SOS-Importer development, you might need to build the OX-F from the branch *develop*. Please check in the `pom.xml` the value of `<oxf.version>`. If it ends with `-SNAPSHOT`, continue here, else continue with step #3:

      ```
      ~$ git clone https://github.com/52North/OX-Framework.git
      ~$ cd OX-Framework
      ~/OX-Framework$ mvn install
      ```

      or this fork (please check for [open pull requests](https://github.com/52North/OX-Framework/pulls)!):

      ```
      ~$ git remote add eike https://github.com/EHJ-52n/OX-Framework.git
      ~/OX-Framework$ git fetch eike
      ~/OX-Framework$ git checkout -b eike-develop eike/develop
      ~/OX-Framework$ mvn clean instal
      ```

   1. Checkout latest version of SOS-Importer with:

      ```
      ~$ git clone https://github.com/52North/sos-importer.git
      ```

      in a separate directory.
   1. Switch to required branch (`master` for latest stable version; `develop` for latest development version) via

      ```
      ~/sos-importer$ git checkout devlop
      ```

      , for example.
   1. Set-Up the geotools repository like this ([maven help regarding repositories](http://maven.apache.org/guides/mini/guide-multiple-repositories.html)):

      ```
      <repository>
  	     <id>osgeo</id>
  	     <name>Open Source Geospatial Foundation Repository</name>
  	     <url>http://download.osgeo.org/webdav/geotools/</url>
      </repository>
      ```

   1. Build SOS importer modules:

      ```
      ~/sos-importer$ mvn install
      ```

   1. Find the jar files here:
      * _wizard_: `~/52n-sos-importer/wizard/target/`
      * _feeder_: `~/52n-sos-importer/feeder/target/`

| *URLs* | *Content* |
| --- | --- |
| git: `https://github.com/52North/sos-importer/tree/develop` | latest development version |
| git: `https://github.com/52North/sos-importer/tree/master` | latest stable version |
| git: `https://github.com/52North/sos-importer/releases/tag/52n-sos-importer-0.4.0` | release 0.4.0 |
| git: `https://github.com/52North/sos-importer/releases/tag/52n-sos-importer-0.3.0` | release 0.3.0 |
| git: `https://github.com/52North/sos-importer/releases/tag/52n-sos-importer-0.2.0` | release 0.2.0 |
| git: `https://github.com/52North/sos-importer/releases/tag/52n-sos-importer-0.1.0` | release 0.1.0 |


## Dependencies

   * JAVA 1.7+
   * List of Dependencies (generated following our [best practice](https://wiki.52north.org/bin/view/Documentation/BestPracticeLicenseManagementInSoftwareProjects#maven_license_plugin_by_codehaus) documentation): [THIRD-PARTY.txt](https://wiki.52north.org/pub/SensorWeb/SosImporter/THIRD-PARTY.txt)


## Extend `CsvParser`

*For providing your own `CsvParser` implementation*

Since version 0.4.0, it is possible to implement your own `CsvParser` type, if the current generic CSV parser implementation is not sufficient for your use case. Currently, one additional parser is implemented. The `NSAMParser` is able to handle CSV files that grow not from top to down but from left to right.

To get your own parser implementation working, you need to implement the `CsvParser` interface (see the next Figure for more details).

<img src="https://wiki.52north.org/pub/SensorWeb/SosImporter/sos-importer_csvparser.png" alt="sos-importer_csvparser.png" width='274' height='165' />

In addition, you need to add `<CsvParserClass>` in your configuration to `<CsvMetadata>`. The class that MUST be used for parsing the data file. The interface `org.n52.sos.importer.feeder.CsvParser` MUST be implemented. The class name MUST contain the fully qualified package name and a zero-argument constructor MUST be provided.

The `CsvParser.init(..)` is called after the constructor and should result in a ready-to-use parser instance. `CsvParser.readNext()` returns the next "line" of values that should be processed as `String[]`. An `IOException` could be thrown if something unexpected happens during the read operation. The `CsvParser.getSkipLimit()` should return 0, if number of lines `` number of observations, or the difference between line number and line index.


# Troubleshooting/Bugs

If you have any problems, please check the issues section for the sos importer first:
   * [All open issues](https://github.com/52North/sos-importer/issues)
   * [All closed issues](https://github.com/52North/sos-importer/issues?utf8=%E2%9C%93&q=is%3Aissue+is%3Aclosed)
   * [All issues](https://github.com/52North/sos-importer/issues?utf8=%E2%9C%93&q=is%3Aissue)


## Branches

This project follows the  [Gitflow branching model](http://nvie.com/posts/a-successful-git-branching-model/). "master" reflects the latest stable release.
Ongoing development is done in branch [develop](../../tree/develop) and dedicated feature branches (feature-*).


## Build Status

* Master: [![Build Status](http://build.dev.52north.org/jenkins/buildStatus/icon?job=sos-importer)](http://build.dev.52north.org/jenkins/view/Sensor%20Web/job/sos-importer/) [![volkswagen status](https://auchenberg.github.io/volkswagen/volkswargen_ci.svg?v=1)](https://github.com/auchenberg/volkswagen)

## License

The 52&deg;North Sos-Importer is published under the [GNU General Public License v2.0](http://www.spdx.org/licenses/GPL-2.0). The licenses of the dependencies are documented in [another section](#Dependencies).
