# Simple Speedtest-Client
Java-library and command line interface (CLI) for testing internet bandwidth using speedtest.net

A simplified Java-implementation of Matt Martz' speedtest-cli (https://github.com/sivel/speedtest-cli)

## Technical requirements:
+ Java 8+
+ Maven

## Usage:
### Build JAR file(s):
Called from command line: 
```bash
mvn clean install
```

### Use CLI:
Called from command line: 
```bash
java -jar simple-speedtest-client-2.0.2.jar
```
```bash
usage: Optional parameters:
-noDownload   Do not perform download-test
-noUpload     Do not perform upload-test
-share        Generate and provide a URL to the speedtest.net share results image
```

Example:
```bash
$ java -jar simple-speedtest-client-2.0.2.jar 
Retrieving speedtest.net configuration...
Testing from M247 Ltd (91.132.139.76)...
Retrieving speedtest.net server list...
Selecting best server based on ping...
Hosted by AMA netwoRX GmbH (Vienna) [10,54 km]: 17,33 ms
Testing download speed........................................
Download: 50,26 Mbits/s
Testing upload speed...................................................
Upload: 19,24 Mbits/s
```

### Use as Java library:
Called from Java:
```java
// Full test can take up to 25 seconds
try {
  SpeedtestResult result = SpeedtestController.runSpeedTest();
} catch (SpeedtestException e) {
  e.printStackTrace();
}
```
#### How to add project dependency to Maven or Gradle:
https://jitpack.io/private#BernhardAngerer/simple-speedtest-client/2.0.2
